package com.catan.main.persistence;

import com.catan.main.datamodel.PersistenceModel;

import java.rmi.ServerException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseAccess<T> implements IAccess<T> {

    //region Fields
    private DatabaseContext dataContext;
    private PreparedStatement statement;
    private List<T> objects;
    //endregion

    public DatabaseAccess(DatabaseContext dataContext) {
        this.dataContext = dataContext;
    }

    //region Properties
    /**
     * @return the dataContext
     */
    @Override
    public DatabaseContext getDataContext() {
        return dataContext;
    }
    /**
     * @param dataContext the dataContext to set
     */
    private void setDataContext(DatabaseContext dataContext) {
        this.dataContext = dataContext;
    }

    /**
     * @return the statement
     */
    public PreparedStatement getStatement() {
        return statement;
    }
    /**
     * @param statement the statement to set
     */
    public void setStatement(PreparedStatement statement) {
        this.statement = statement;
    }

    public List<T> getObjects() {
        return objects;
    }
    private void setObjects(List<T> objects) {
        this.objects = objects;
    }
    //endregion

    //region Crud
    /**
     * returns all objects of the specified type
     * @throws DataAccessException
     */
    public List<T> getAll() throws DataAccessException {

        ArrayList<T> list = new ArrayList<T>();
        ResultSet reader = getDataContext().get(getSelectStatement());

        try {
            while (reader.next()) {
                list.add(initialize(reader, list));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                reader.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return list;
    }

    /**
     * gets the object with the given id
     * @param id int the id of the desired object
     * @return T
     * @throws DataAccessException
     */
    public T get(int id) throws DataAccessException {

        ResultSet reader = getDataContext().get(getSingleSelectStatement(id));

        try {
            if (reader.next()) {
                return initialize(reader, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                reader.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return null;
    }

    /**
     * Inserts a new object into the database
     * @param input the object
     * @throws DataAccessException
     */
    @Override
    public int insert(T input) throws DataAccessException {
        int id = getDataContext().execute(getInsertStatement(input), DataContext.MethodType.INSERT);
        PersistenceModel model = (PersistenceModel) input;
        model.setId((long)id);
        return id;
    }

    /**
     * Updates the provided object in the database
     * @param input the object
     * @throws DataAccessException
     */
    @Override
    public void update(T input) throws DataAccessException {
        getDataContext().execute(getUpdateStatement(input), DataContext.MethodType.UPDATE);
    }

    /**
     * Deletes the provided object from the database
     * @param input the object
     * @throws DataAccessException
     */
    @Override
    public void delete(T input) throws DataAccessException {
        getDataContext().execute(getDeleteStatement(input), DataContext.MethodType.DELETE);
    }
    //endregion

    //region Abstract Methods
    /**
     * prepares the sql statement with the appropriate select parameters
     * @return PreparedStatement
     */
    protected abstract PreparedStatement getSelectStatement();

    /**
     * prepares the sql statement with select parameters for a single get
     * @param id the id
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getSingleSelectStatement(int id)
            throws DataAccessException;

    /**
     * prepares the sql statement with the appropriate insert parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getInsertStatement(T input)
            throws DataAccessException;

    /**
     * prepares the sql statement with the appropriate update parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getUpdateStatement(T input)
            throws DataAccessException;

    /**
     * prepares the sql statement with the appropriate delete parameters
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getDeleteStatement(T input)
            throws DataAccessException;

    /**
     * checks all of the parameters of the object to verify their validity
     * @param input the input object
     * @return PreparedStatement
     */
    protected abstract boolean checkParameters(T input);

    /**
     * initializes an object of type T with given resultSet
     * @param reader the reader
     * @param list the list
     * @return T
     * @throws DataAccessException
     * @throws SQLException
     */
    protected abstract T initialize(ResultSet reader, List<T> list)
            throws DataAccessException, SQLException;
    //endregion

    //region Methods
    public boolean isDirty() {
        return true;
    }
    //endregion
}
