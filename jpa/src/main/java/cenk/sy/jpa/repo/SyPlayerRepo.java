package cenk.sy.jpa.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.OzCurrency;

@Repository
public interface SyPlayerRepo extends JpaRepository<SyPlayer, Long> {

	List<SyPlayer> findByDateGreaterThanEqualAndRateFromAndRateTo(LocalDate date, OzCurrency from, OzCurrency to);
	
	List<SyPlayer> findByDateGreaterThanEqualAndDateLessThanEqualAndRateFromAndRateTo(LocalDate since, LocalDate until, OzCurrency from, OzCurrency to);
	
	List<SyPlayer> findByDateGreaterThanEqualAndDateLessThanEqual(LocalDate since, LocalDate until);

	List<SyPlayer> findByIdAndDate(Long tId, LocalDate date);
	
	List<SyPlayer> findByDate(LocalDate date);
}