package cenk.sy.jpa.entity.team;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import cenk.sy.jpa.common.SyEntity;
import cenk.sy.jpa.entity.contract.SyContract;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "contracts" })
public class SyTeam implements SyEntity {

	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "team" })
	private List<SyContract> contracts;

	@ManyToOne
	private SyCurrency currency;

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	private Long id;

	@Column
	private String name;

	public SyTeam() {
	}

	public SyTeam(String name, SyCurrency cur) {
		this.name = name;
		this.currency = cur;
	}

}