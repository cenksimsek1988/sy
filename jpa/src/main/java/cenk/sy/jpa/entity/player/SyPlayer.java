package cenk.sy.jpa.entity.player;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import cenk.sy.jpa.common.OzEntity;
import cenk.sy.jpa.entity.team.SyTeam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "rate", "id"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SyPlayer implements OzEntity{

	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private LocalDate date;
	
	@Column
	private float amount;
	
	@ManyToMany
	private List<SyTeam> teams;
	
}
