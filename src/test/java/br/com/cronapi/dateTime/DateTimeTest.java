package br.com.cronapi.dateTime;

import cronapi.Var;
import cronapi.dateTime.Operations;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTest {

  private static TimeZone last;

  @BeforeEach
  public void before() {
    last = TimeZone.getDefault();
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @AfterAll
  public static void after() {
    TimeZone.setDefault(last);
  }

  private Var getTestDate() {
    return Var.valueOf("2012-05-12T04:05:24Z");
  }

  private Var getSecondTestDate() {
    return cronapi.dateTime.Operations.newDate(Var.valueOf(2012), Var.valueOf(5), Var.valueOf(12), Var.valueOf(4), Var.valueOf(6), Var.valueOf(24));
  }

  private Var getThirdTestDate() {
    return cronapi.dateTime.Operations.newDate(Var.valueOf(2012), Var.valueOf(5), Var.valueOf(13), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
  }

  private Var getFourthTestDate() {
    return cronapi.dateTime.Operations.newDate(Var.valueOf(2012), Var.valueOf(6), Var.valueOf(12), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
  }

  private Var getFifthTestDate() {
    return cronapi.dateTime.Operations.newDate(Var.valueOf(2014), Var.valueOf(5), Var.valueOf(12), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
  }

  @Test
  public void testUpdateDate() {
    Var item = getTestDate();
    item = cronapi.dateTime.Operations.updateDate(item, Var.valueOf(2012), Var.valueOf(5), Var.valueOf(12), Var.valueOf(12), Var.valueOf(5), Var.valueOf(24), Var.valueOf(100));
    assertNotNull(item);
  }

  @Test
  public void testUpdateNewDate() {
    Var item = getTestDate();
    item = cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("day"), Var.valueOf(-230));
    assertNotNull(item);
  }


  @Test
  public void testGetParts() {
    assertEquals((int) cronapi.dateTime.Operations.getDay(getTestDate()).getObjectAsInt(), 12);
    assertEquals((int) cronapi.dateTime.Operations.getMonth(getTestDate()).getObjectAsInt(), 5);
    assertEquals((int) cronapi.dateTime.Operations.getYear(getTestDate()).getObjectAsInt(), 2012);
    assertEquals((int) cronapi.dateTime.Operations.getHour(getTestDate()).getObjectAsInt(), 4);
    assertEquals((int) cronapi.dateTime.Operations.getMinute(getTestDate()).getObjectAsInt(), 5);
    assertEquals((int) cronapi.dateTime.Operations.getSecond(getTestDate()).getObjectAsInt(), 24);
    assertEquals((int) cronapi.dateTime.Operations.getDayOfWeek(getTestDate()).getObjectAsInt(), 7);
  }

  @Test
  public void testDiffDates() {
    assertEquals((int) cronapi.dateTime.Operations.getSecondsBetweenDates(getSecondTestDate(), getTestDate()).getObjectAsInt(), 60);
    assertEquals((int) cronapi.dateTime.Operations.getMinutesBetweenDates(getSecondTestDate(), getTestDate()).getObjectAsInt(), 1);
    assertEquals((int) cronapi.dateTime.Operations.getHoursBetweenDates(getSecondTestDate(), getTestDate()).getObjectAsInt(), 0);
    assertEquals((int) cronapi.dateTime.Operations.getDaysBetweenDates(getThirdTestDate(), getTestDate()).getObjectAsInt(), 1);
    assertEquals((int) cronapi.dateTime.Operations.getMonthsBetweenDates(getFourthTestDate(), getTestDate()).getObjectAsInt(), 1);
    assertEquals((int) cronapi.dateTime.Operations.getYearsBetweenDates(getFifthTestDate(), getTestDate()).getObjectAsInt(), 2);
  }

  @Test
  public void testInc() {
    Var date = cronapi.dateTime.Operations.incSeconds(getTestDate(), Var.valueOf(1));
    assertEquals((int) Operations.getSecond(date).getObjectAsInt(), 25);
    date = cronapi.dateTime.Operations.incMinute(getTestDate(), Var.valueOf(1));
    assertEquals((int) Operations.getMinute(date).getObjectAsInt(), 6);
    date = cronapi.dateTime.Operations.incHour(getTestDate(), Var.valueOf(1));
    assertEquals((int) Operations.getHour(date).getObjectAsInt(), 5);
    date = cronapi.dateTime.Operations.incDay(getTestDate(), Var.valueOf(1));
    assertEquals((int) Operations.getDay(date).getObjectAsInt(), 13);
    date = cronapi.dateTime.Operations.incMonth(getTestDate(), Var.valueOf(1));
    assertEquals((int) Operations.getMonth(date).getObjectAsInt(), 6);
    date = cronapi.dateTime.Operations.incYear(getTestDate(), Var.valueOf(1));
    assertEquals((int) Operations.getYear(date).getObjectAsInt(), 2013);
  }

  @Test
  public void testForm() {
    assertEquals(Operations.formatDateTime(getTestDate(), Var.valueOf("dd/MM/yyyy HH:mm:ss")).getObjectAsString(), "12/05/2012 04:05:24");
  }

  @Test
  public void testUpdateDateValues() {
    Var item = getTestDate();
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("year"), Var.valueOf(5)).getObjectAsDateTime().get(Calendar.YEAR), 5);
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("month"), Var.valueOf(6)).getObjectAsDateTime().get(Calendar.MONTH), 5);
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("day"), Var.valueOf(21)).getObjectAsDateTime().get(Calendar.DAY_OF_MONTH), 21);
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("hour"), Var.valueOf(4)).getObjectAsDateTime().get(Calendar.HOUR_OF_DAY), 4);
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("minute"), Var.valueOf(15)).getObjectAsDateTime().get(Calendar.MINUTE), 15);
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("second"), Var.valueOf(15)).getObjectAsDateTime().get(Calendar.SECOND), 15);
    assertEquals(cronapi.dateTime.Operations.updateNewDate(item, Var.valueOf("millisecond"), Var.valueOf(54)).getObjectAsDateTime().get(Calendar.MILLISECOND), 54);
  }

  @Test
  void testGetLastDayFromMonth(){
    Var date = getTestDate();
    Var retorno = Operations.getLastDayFromMonth(Var.valueOf(Operations.getMonth(date)), Var.valueOf(Operations.getYear(date)));
    assertEquals(retorno.getObjectAsInt(), 31);
  }

  @Test
  void getMonthsBetweenDates(){
    Var dateBegin = cronapi.dateTime.Operations.newDate(Var.valueOf(2012), Var.valueOf(6), Var.valueOf(12), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
    Var dateEnd = cronapi.dateTime.Operations.newDate(Var.valueOf(2012), Var.valueOf(4), Var.valueOf(20), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
    assertEquals((int) cronapi.dateTime.Operations.getMonthsBetweenDates(dateBegin, dateEnd).getObjectAsInt(), 1);
    dateBegin = cronapi.dateTime.Operations.newDate(Var.valueOf(2012), Var.valueOf(7), Var.valueOf(12), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
    assertEquals(cronapi.dateTime.Operations.getMonthsBetweenDates(dateEnd, dateBegin).getObjectAsInt(), -2);
    dateEnd = cronapi.dateTime.Operations.newDate(Var.valueOf(2013), Var.valueOf(2), Var.valueOf(20), Var.valueOf(4), Var.valueOf(5), Var.valueOf(24));
    assertEquals((int) cronapi.dateTime.Operations.getYearsBetweenDates(dateEnd, dateBegin).getObjectAsInt(), 1);
  }

  @Test
  void getNowNoHour(){
    Var noHour = cronapi.dateTime.Operations.getNowNoHour();
    assertEquals(noHour.getObjectAsDateTime().getTime().getHours(), 0);
    assertEquals(noHour.getObjectAsDateTime().getTime().getMinutes(), 0);
    assertEquals(noHour.getObjectAsDateTime().getTime().getSeconds(), 0);
  }

  @Test
  void getNowInMilliseconds(){
    Var nowInMilliseconds = cronapi.dateTime.Operations.getNowInMilliseconds();
    assertNotNull(nowInMilliseconds);
  }

}

