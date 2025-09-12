package odyssey.backend.roadmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import odyssey.backend.domain.auth.User;
import odyssey.backend.global.RestDocsSupport;
import odyssey.backend.presentation.roadmap.dto.request.RoadmapRequest;
import odyssey.backend.presentation.roadmap.dto.response.TeamRoadmapResponse;
import odyssey.backend.shared.test.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamRoadmapControllerTest extends RestDocsSupport {

    @Test
    void 팀_로드맵을_조회한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long teamId = 1L;

        TeamRoadmapResponse response1 = new TeamRoadmapResponse(
                1L, "팀 타이틀1", "팀 설명1", List.of("카테고리1"),
                "https://team-image1.com", LocalDate.now(), LocalDateTime.now(), false,
                testUser.getUuid(), 1L, "이건우"
        );

        TeamRoadmapResponse response2 = new TeamRoadmapResponse(
                2L, "팀 타이틀2", "팀 설명2", List.of("카테고리2"),
                "https://team-image2.com", LocalDate.now(), LocalDateTime.now(), true,
                testUser.getUuid(), 1L, "이건우"
        );

        given(roadmapService.findTeamRoadmaps(testUser, teamId))
                .willReturn(List.of(response1, response2));

        mvc.perform(get("/teams/{teamId}/roadmap", teamId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("roadmap-get-team",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data[].id").description("로드맵 ID"),
                                fieldWithPath("data[].title").description("로드맵 제목"),
                                fieldWithPath("data[].description").description("로드맵 설명"),
                                fieldWithPath("data[].categories").description("카테고리 리스트"),
                                fieldWithPath("data[].thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data[].lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data[].lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data[].isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data[].uuid").description("작성자 UUID"),
                                fieldWithPath("data[].teamId").description("팀 ID"),
                                fieldWithPath("data[].teamName").description("팀 이름")
                        )
                ));
    }

    @Test
    void 팀_로드맵을_생성한다() throws Exception {
        User testUser = UserCreate.createUser();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(testUser, null));

        Long teamId = 1L;

        RoadmapRequest request = new RoadmapRequest(
                "팀 타이틀 생성",
                "팀 설명 생성",
                List.of("카테고리1", "카테고리2"),
                null
        );

        MockMultipartFile thumbnail = new MockMultipartFile(
                "thumbnail",
                "thumbnail.png",
                MediaType.IMAGE_PNG_VALUE,
                "테스트 이미지".getBytes()
        );

        MockMultipartFile roadmapPart = new MockMultipartFile(
                "roadmap",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().writeValueAsBytes(request)
        );

        TeamRoadmapResponse createdResponse = new TeamRoadmapResponse(
                1L,
                request.getTitle(),
                request.getDescription(),
                request.getCategories(),
                "https://team-image.com",
                LocalDate.now(),
                LocalDateTime.now(),
                false,
                testUser.getUuid(),
                teamId,
                "이건우팀"
        );

        given(roadmapFacade.saveTeamRoadmap(
                any(RoadmapRequest.class),
                any(MultipartFile.class),
                any(User.class),
                eq(teamId)
        )).willReturn(createdResponse);

        mvc.perform(multipart("/teams/{teamId}/roadmap", teamId)
                        .file(thumbnail)
                        .file(roadmapPart)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("roadmap-create-team",
                        requestParts(
                                partWithName("roadmap").description("로드맵 정보"),
                                partWithName("thumbnail").description("썸네일 이미지")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data.id").description("생성된 로드맵 ID"),
                                fieldWithPath("data.title").description("로드맵 제목"),
                                fieldWithPath("data.description").description("로드맵 설명"),
                                fieldWithPath("data.categories").description("카테고리 리스트"),
                                fieldWithPath("data.thumbnailUrl").description("썸네일 URL"),
                                fieldWithPath("data.lastModifiedAt").description("마지막 수정 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("data.lastAccessedAt").description("마지막 접속 일시 (yyyy-MM-ddTHH:mm:ss)"),
                                fieldWithPath("data.isFavorite").description("즐겨찾기 여부"),
                                fieldWithPath("data.uuid").description("작성자 UUID"),
                                fieldWithPath("data.teamId").description("팀 ID"),
                                fieldWithPath("data.teamName").description("팀 이름")
                        )
                ));
    }


}
