package cenk.sy.jpa.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cenk.sy.jpa.entity.team.OzCurrency;
import cenk.sy.jpa.entity.team.SyTeam;

@Repository
public interface SyTeamRepo extends JpaRepository<SyTeam, Long> {

	SyTeam findById(long id);

	SyTeam findByDateAndFromAndTo(LocalDate date, OzCurrency from, OzCurrency to);
	
	List<SyTeam> findByDateGreaterThanEqualAndDateLessThanEqualAndFromAndTo(LocalDate since, LocalDate until, OzCurrency from, OzCurrency to);
	
	List<SyTeam> findByDateGreaterThanEqualAndDateLessThanEqual(LocalDate since, LocalDate until);
}