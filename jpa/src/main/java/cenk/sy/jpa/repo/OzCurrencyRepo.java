package cenk.sy.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cenk.sy.jpa.entity.team.OzCurrency;

@Repository
public interface OzCurrencyRepo extends JpaRepository<OzCurrency, Long> {

	List<OzCurrency> findAll();
}