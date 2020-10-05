package cenk.sy.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cenk.sy.jpa.entity.player.SyPlayer;

@Repository
public interface SyPlayerRepo extends JpaRepository<SyPlayer, Long> {

}