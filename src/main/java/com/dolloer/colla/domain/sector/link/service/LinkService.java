package com.dolloer.colla.domain.sector.link.service;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import com.dolloer.colla.domain.project.entity.ProjectMember;
import com.dolloer.colla.domain.project.entity.ProjectRole;
import com.dolloer.colla.domain.project.repository.ProjectMemberRepository;
import com.dolloer.colla.domain.project.repository.ProjectRepository;
import com.dolloer.colla.domain.sector.link.dto.request.LinkCreateRequest;
import com.dolloer.colla.domain.sector.link.dto.request.LinkUpdateRequest;
import com.dolloer.colla.domain.sector.link.dto.response.LinkListResponse;
import com.dolloer.colla.domain.sector.link.dto.response.LinkResponse;
import com.dolloer.colla.domain.sector.link.entity.Link;
import com.dolloer.colla.domain.sector.link.repository.LinkRepository;
import com.dolloer.colla.response.exception.CustomException;
import com.dolloer.colla.response.response.ApiResponseLinkEnum;
import com.dolloer.colla.response.response.ApiResponseProjectEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;


    @Transactional
    public void createLink(Member member, Long projectId, LinkCreateRequest linkCreateRequest) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        // 동일한 링크가 존재하는지 체크
        boolean exists = linkRepository.existsByProjectAndUrl(project, linkCreateRequest.getUrl());
        if (exists) {
            throw new CustomException(ApiResponseLinkEnum.DUPLICATED_LINK);
        }

        Link createdLink = new Link(linkCreateRequest.getTitle(), linkCreateRequest.getUrl(), linkCreateRequest.getDescription(), project, member);
        linkRepository.save(createdLink);

    }


    public LinkListResponse getLinkList(Member member, Long projectId) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        List<Link> links = linkRepository.findAllByProject(project);

        List<LinkResponse> linkList = links.stream()
                .map(link -> new LinkResponse(
                        link.getId(),
                        link.getLinkTitle(),
                        link.getDescription(),
                        link.getUrl(),
                        link.getUploader().getUsername(),
                        link.getCreatedAt().toLocalDate(),
                        link.getUpdatedAt().toLocalDate()
                ))
                .toList();

        return new LinkListResponse(linkList);
    }

    public LinkResponse getLink(Member member, Long projectId, Long linkId) {
        Project project = checkProject(projectId);
        checkRelation(project, member);
        Link link = checkLink(linkId);

        return new LinkResponse(
                link.getId(),
                link.getLinkTitle(),
                link.getDescription(),
                link.getUrl(),
                link.getUploader().getUsername(),
                link.getCreatedAt().toLocalDate(),
                link.getUpdatedAt().toLocalDate()
        );
    }

    // 링크 수정하기
    @Transactional
    public LinkResponse updateLink(Member member, Long projectId, Long linkId, LinkUpdateRequest linkUpdateRequest) {
        Project project = checkProject(projectId);
        checkRelation(project, member);
        Link link = checkLink(linkId);

        // 작성자만 수정 가능
        if (!link.getUploader().getId().equals(member.getId())) {
            throw new CustomException(ApiResponseProjectEnum.NOT_ENOUGH_PERMISSION);
        }

        if(linkUpdateRequest.getDescription() != null){link.updateDescription(linkUpdateRequest.getDescription());}
        if(linkUpdateRequest.getUrl()!= null){link.updateUrl(linkUpdateRequest.getUrl());}
        if(linkUpdateRequest.getTitle() != null){link.updateTitle(linkUpdateRequest.getTitle());}

        return new LinkResponse(
                link.getId(),
                link.getLinkTitle(),
                link.getDescription(),
                link.getUrl(),
                link.getUploader().getUsername(),
                link.getCreatedAt().toLocalDate(),
                link.getUpdatedAt().toLocalDate()
        );
    }

    // 링크 삭제
    @Transactional
    public void deleteLink(Member member, Long projectId, Long linkId) {
        Project project = checkProject(projectId);
        ProjectMember projectMember = checkRelation(project, member);
        Link link = checkLink(linkId);

        boolean isUploader = member.equals(link.getUploader());
        boolean isManager = projectMember.getRole() == ProjectRole.OWNER || projectMember.getRole() == ProjectRole.ADMIN;

        // 업로더거나 매니저만 삭제 가능
        if (!isUploader && !isManager) {
            throw new CustomException(ApiResponseLinkEnum.NOT_ENOUGH_PERMISSION);
        }

        linkRepository.delete(link);
    }

    public LinkListResponse searchLinksByTitle(Member member, Long projectId, String keyword) {
        Project project = checkProject(projectId);
        checkRelation(project, member);

        List<Link> links = linkRepository.searchByTitle(project, keyword);

        List<LinkResponse> linkList = links.stream()
                .map(link -> new LinkResponse(
                        link.getId(),
                        link.getLinkTitle(),
                        link.getDescription(),
                        link.getUrl(),
                        link.getUploader().getUsername(),
                        link.getCreatedAt().toLocalDate(),
                        link.getUpdatedAt().toLocalDate()
                ))
                .toList();

        return new LinkListResponse(linkList);
    }


    // 프로젝트 존재 확인
    private Project checkProject(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.PROJECT_NOT_EXIST));
    }
    // 링크 존재 확인
    private Link checkLink(Long linkId){
        return linkRepository.findById(linkId)
                .orElseThrow(() -> new CustomException(ApiResponseLinkEnum.LINK_NOT_EXIST));

    }
    // 유저가 프로젝트에 속한지 확인
    private ProjectMember checkRelation(Project project, Member member ){
        return projectMemberRepository.findByProjectAndMember(project, member)
                .orElseThrow(() -> new CustomException(ApiResponseProjectEnum.NOT_THIS_PROJECT_MEMBER));
    }


}
