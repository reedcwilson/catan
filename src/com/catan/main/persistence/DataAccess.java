package com.catan.main.persistence;

import com.catan.main.datamodel.PersistenceModel;

import java.io.Closeable;
import java.util.List;

public abstract class DataAccess<T extends PersistenceModel, PreparedStatement> {

    //region Fields
    private PreparedStatement statement;
    private List<T> objects;
    private boolean isDirty;
    //endregion

    public DataAccess() {
        isDirty = true;
    }

    //region Properties

    /**
     * @return the dataContext
     */
    public abstract DataContext getDataContext();

    /**
     * @return ObjectCreator the initializer of choice
     */
    public abstract ObjectCreator getObjectCreator();

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

    public List<T> getObjects() throws DataAccessException {
        if (isDirty) {
            return getAll();
        }
        return objects;
    }

    public void setObjects(List<T> objects) {
        isDirty = false;
        this.objects = objects;
    }
    //endregion

    //region Crud

    /**
     * returns all objects of the specified type
     *
     * @throws DataAccessException
     */
    public List<T> getAll() throws DataAccessException {
        if (isDirty) {
            setObjects(getDataContext().get(getSelectStatement(), getObjectCreator()));
        }
        return objects;
    }

    /**
     * gets the object with the given id
     *
     * @param id int the id of the desired object
     * @return T
     * @throws DataAccessException
     */
    public T get(int id) throws DataAccessException {
        if (isDirty) {
            setObjects(getDataContext().get(getSingleSelectStatement(id), getObjectCreator()));
        }
        for (T t : objects) {
            if (t.getId().intValue() == id) {
                return t;
            }
        }
        return null;
    }

    /**
     * Inserts a new object into the database
     *
     * @param input the object
     * @throws DataAccessException
     */
    public int insert(T input) throws DataAccessException {
        isDirty = true;
        int id = getDataContext().execute(getInsertStatement(input), DataContext.MethodType.INSERT);
        PersistenceModel model = input;
        model.setId((long) id);
        return id;
    }

    /**
     * Updates the provided object in the database
     *
     * @param input the object
     * @throws DataAccessException
     */
    public void update(T input) throws DataAccessException {
        isDirty = true;
        getDataContext().execute(getUpdateStatement(input), DataContext.MethodType.UPDATE);
    }

    /**
     * Deletes the provided object from the database
     *
     * @param input the object
     * @throws DataAccessException
     */
    public void delete(T input) throws DataAccessException {
        isDirty = true;
        getDataContext().execute(getDeleteStatement(input), DataContext.MethodType.DELETE);
    }
    //endregion

    //region Abstract Methods

    /**
     * prepares the sql statement with the appropriate select parameters
     *
     * @return PreparedStatement
     */
    protected abstract PreparedStatement getSelectStatement();

    /**
     * prepares the sql statement with select parameters for a single get
     *
     * @param id the id
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getSingleSelectStatement(int id)
            throws DataAccessException;

    /**
     * prepares the sql statement with the appropriate insert parameters
     *
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getInsertStatement(T input)
            throws DataAccessException;

    /**
     * prepares the sql statement with the appropriate update parameters
     *
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getUpdateStatement(T input)
            throws DataAccessException;

    /**
     * prepares the sql statement with the appropriate delete parameters
     *
     * @param input the input object
     * @return PreparedStatement
     * @throws DataAccessException
     */
    protected abstract PreparedStatement getDeleteStatement(T input)
            throws DataAccessException;

    /**
     * checks all of the parameters of the object to verify their validity
     *
     * @param input the input object
     * @return PreparedStatement
     */
    protected abstract boolean checkParameters(T input);

    //endregion

    //region Methods
    public boolean isDirty() {
        return isDirty;
    }
    public void setIsDirty(boolean dirty) {
        this.isDirty = dirty;
    }
    //endregion
}
