package br.com.cronapi.chart;

import cronapi.Var;
import cronapi.chart.Operations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationsTest {

    @Test
    void testCreateChart() throws Exception {
        Operations.createChart(new Var(null, "object"), new Var("id", "object"), new Var("id", "object"), new Var("id", "object"), new Var(null, "object"));
    }

    @Test
    void testCreateChartSerie() throws Exception {
        Var result = Operations.createChartSerie(new Var("id", "object"), new Var("id", "object"), new Var("id", "object"));
        Assertions.assertEquals("{\"label\":\"object\",\"data\":\"object\",\"options\":\"object\"}", result.toString());
    }
}