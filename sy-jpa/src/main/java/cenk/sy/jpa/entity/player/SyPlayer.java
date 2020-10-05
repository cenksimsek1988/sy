package cenk.sy.jpa.entity.player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import cenk.sy.jpa.common.SyEntity;
import cenk.sy.jpa.entity.contract.SyContract;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "contracts" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SyPlayer implements SyEntity {

	@Column
	private Integer birthYear;

	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "player" })
	@JsonSetter
	private List<SyContract> contracts = new ArrayList<>();

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	private Long id;

	@Column
	private String name;

	public SyPlayer() {
	}

	public SyPlayer(String name, Integer birthYear) {
		this.name = name;
		this.birthYear = birthYear;
	}

}
