package cenk.sy.jpa.entity.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import cenk.sy.jpa.common.SyEntity;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.SyTeam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SyContract implements SyEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties("contracts")
	private SyPlayer player;

	@ManyToOne
	@JsonIgnoreProperties("contracts")
	private SyTeam team;

	@Column
	private Integer year;

}
