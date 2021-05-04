package com.chat.backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "room_users",
				joinColumns = @JoinColumn(name = "room_id"),
				inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> connections = new HashSet<>();

	@CreationTimestamp
	private LocalDateTime createdAt;
}
