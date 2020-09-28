package cenk.sy.jpa.entity.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import cenk.sy.jpa.common.OzEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@Entity
@Table
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"})
public class OzCurrency implements OzEntity {
	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true)
	@EqualsAndHashCode.Include
	private String code;
	
}
