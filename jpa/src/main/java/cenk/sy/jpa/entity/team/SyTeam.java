package cenk.sy.jpa.entity.team;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import cenk.sy.jpa.common.OzEntity;
import cenk.sy.jpa.entity.player.SyPlayer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "id", "date", "from", "to"})
public class SyTeam implements OzEntity {

	@Id
	@GeneratedValue
	private long id;
	
	@Column
	@EqualsAndHashCode.Include
	private LocalDate date;
	
	@ManyToMany
	@EqualsAndHashCode.Include
	private List<SyPlayer> players;
	
	@ManyToOne
	@EqualsAndHashCode.Include
	private OzCurrency to;
	
	@Column
	private float rate;
	
}