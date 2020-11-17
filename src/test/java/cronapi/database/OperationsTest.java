package cronapi.database;

import cronapi.Var;
import io.cronapp.testing.northwind.NorthwindExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(NorthwindExtension.class)
@DisplayName("Banco de Dados")
class OperationsTest {

  @Test
  @DisplayName("CRONAPP-2287 - Bloco retorna todas as colunas mesmo que na query só tenha duas informadas")
  void cronapp2287() throws Exception {
    var query = Operations.executeNativeQuery(Var.valueOf("io.cronapp.testing.northwind.entity.Customer"), Var.valueOf("select customer_id from customers"));
    assertThat(cronapi.list.Operations.size(query)).isGreaterThan(Var.valueOf(1));

    var entity = cronapi.list.Operations.get(query, Var.valueOf(1));
    assertThat(cronapi.logic.Operations.isNull(entity.getField("customerId"))).isEqualTo(Var.VAR_FALSE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("address"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("city"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("companyName"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("contactName"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("contactTitle"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("country"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("fax"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("phone"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("postalCode"))).isEqualTo(Var.VAR_TRUE);
    assertThat(cronapi.logic.Operations.isNull(entity.getField("region"))).isEqualTo(Var.VAR_TRUE);
  }
}