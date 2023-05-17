package cart.repository;

import cart.domain.member.Member;
import cart.domain.member.MemberId;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MemberRepository {
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert jdbcInsert;

	private final RowMapper<Member> memberRowMapper =
		(resultSet, rowNum) ->
			new Member(
				MemberId.from(resultSet.getLong("id")),
				resultSet.getString("name"),
				resultSet.getString("email"),
				resultSet.getString("password")
			);

	public MemberRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("members")
			.usingGeneratedKeyColumns("id");
	}

	public MemberId insert(final Member member) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(member);
		long id = jdbcInsert.executeAndReturnKey(params).longValue();
		return MemberId.from(id);
	}

	public List<Member> findAll() {
		final String sql = "SELECT * FROM members";
		return jdbcTemplate.query(sql, memberRowMapper);
	}

	public Member findByMemberId(final MemberId memberId) {
		final String sql = "SELECT * FROM members WHERE id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, memberRowMapper, memberId.getValue());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Member findByEmail(final String email) {
		final String sql = "SELECT * FROM members WHERE email = ?";
		try {
			return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
