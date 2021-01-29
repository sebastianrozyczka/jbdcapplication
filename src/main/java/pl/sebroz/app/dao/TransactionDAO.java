package pl.sebroz.app.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import pl.sebroz.app.model.Transaction;
import pl.sebroz.app.model.Type;

import java.util.List;

@Repository
public class TransactionDAO {
    private static final String DELETE = "DELETE FROM transactions WHERE id = ?";
    private static final String SELECT = "SELECT * FROM transactions";
    private static final String SELECT_FOR_ID = "SELECT * FROM transactions WHERE id = ?";
    private static final String SELECT_FOR_TYPE = "SELECT * FROM transactions WHERE type=";
    private static final String UPDATE = "UPDATE transactions SET type=:type, description=:description, amount=:amount, date=:date WHERE id=:id";
    private static final String TRANSACTIONS = "transactions";
    private static final String TYPE = "type";
    private static final String DESCRIPTION = "description";
    private static final String AMOUNT = "amount";
    private static final String DATE = "date";

    private final JdbcTemplate jdbcTemplate;

    public TransactionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> listByType(Type type) {
        if (type != null) {
            return jdbcTemplate.query(SELECT_FOR_TYPE + type,
                    BeanPropertyRowMapper.newInstance(Transaction.class));
        }
        return listAll();
    }

    public List<Transaction> listAll() {
        return jdbcTemplate.query(SELECT,
                BeanPropertyRowMapper.newInstance(Transaction.class));
    }

    public void save(Transaction transaction) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TRANSACTIONS).usingColumns(TYPE, DESCRIPTION, AMOUNT, DATE);
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(transaction);

        jdbcInsert.execute(param);
    }

    public Transaction get(int id) {
        Object[] args = {id};

        return jdbcTemplate.queryForObject(SELECT_FOR_ID, BeanPropertyRowMapper.newInstance(Transaction.class), args);
    }

    public void update(Transaction transaction) {
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(transaction);

        jdbcTemplate.update(UPDATE, param);
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE, id);
    }
}

