package app.dao;

import app.entity.TestDataBaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("TestDataBaseTypeDAO")
@Transactional(transactionManager="app-TransactionManager")
public interface TestDataBaseTypeDAO extends JpaRepository<TestDataBaseType, String> {

    /**
     * Obtém a instância de TestDataBaseType utilizando os identificadores
     *
     * @param id
     *          Identificador
     * @return Instância relacionada com o filtro indicado
     * @generated
     */
    @Query("SELECT entity FROM TestDataBaseType entity WHERE entity.id = :id")
    TestDataBaseType findOne(@Param(value="id") java.lang.String id);

    /**
     * Remove a instância de TestDataBaseType utilizando os identificadores
     *
     * @param id
     *          Identificador
     * @return Quantidade de modificações efetuadas
     * @generated
     */
    @Modifying
    @Query("DELETE FROM TestDataBaseType entity WHERE entity.id = :id")
    void delete(@Param(value="id") java.lang.String id);

}

