package cenk.sy.jpa.entity.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cenk.sy.jpa.common.SyEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SyCurrency implements SyEntity {

	@Column(unique = true)
	@EqualsAndHashCode.Include
	private String code;

	@Id
	@GeneratedValue
	private Long id;

	public SyCurrency() {
	}

	public SyCurrency(Long id) {
		this.id = id;
	}

}
