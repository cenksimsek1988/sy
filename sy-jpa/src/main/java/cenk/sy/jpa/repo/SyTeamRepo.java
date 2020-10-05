package cenk.sy.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cenk.sy.jpa.entity.team.SyTeam;

@Repository
public interface SyTeamRepo extends JpaRepository<SyTeam, Long> {

}