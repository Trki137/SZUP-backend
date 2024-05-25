package infsus.szup.dataloader;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import infsus.szup.repository.TeamMemberDao;
import infsus.szup.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(5)
public class TeamMembersDataLoader implements CommandLineRunner {
    private final ObjectMapper objectMapper;
    private final TeamService teamService;
    private final TeamMemberDao teamMemberDao;
    private static final String DATA_PATH = "/data/team-members.json";

    @Override
    public void run(String... args) {
        if (teamMemberDao.count() == 0) {
            log.info("Loading team members into database from JSON: {}", DATA_PATH);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(DATA_PATH)) {
                TeamMembers response = objectMapper.readValue(inputStream, TeamMembers.class);
                response.teamMembers.forEach(member -> teamService.addMember(member.projectId, member.teamId, member.memberId, member.addedByUserId));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read JSON data", e);
            }
        }
    }

    private record TeamMember(Long projectId, Long teamId, Long memberId, Long addedByUserId) {
    }

    private record TeamMembers(List<TeamMember> teamMembers) {
    }
}
