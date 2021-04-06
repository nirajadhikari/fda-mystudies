package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.dao.StudyDAO;
import com.fdahpstudydesigner.util.StudyExportSqlQueries;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudyExportService {

  @Autowired StudyDAO studyDao;

  public void exportStudy(String studyId) throws SQLException {
    List<String> insertSqlStatements = new ArrayList<>();
    StudyBo studyBo = studyDao.getStudy(studyId);

    addStudyInsertSql(studyBo, insertSqlStatements);
  }

  private void addStudyInsertSql(StudyBo studyBo, List<String> insertSqlStatements)
      throws SQLException {
    String studiesInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDIES, studyBo.getId(), "pass other values from BO");

    insertSqlStatements.add(studiesInsertQuery);
  }

  private String prepareInsertQuery(String sqlQuery, String... values) throws SQLException {
    String[] columns =
        sqlQuery
            .substring(sqlQuery.indexOf('(') + 1, sqlQuery.indexOf(")"))
            .replace("`", "")
            .split(",");

    if (columns.length != values.length) {
      throw new SQLException("Column count doesn't match value count.");
    }

    int i = 0;
    for (String column : columns) {
      if (StringUtils.isNumeric(values[i])) {
        sqlQuery = sqlQuery.replaceFirst("<" + column + ">", values[i]);
      } else {
        sqlQuery = sqlQuery.replaceFirst("<" + column + ">", "'" + values[i] + "'");
      }

      i++;
    }

    return sqlQuery;
  }
}
