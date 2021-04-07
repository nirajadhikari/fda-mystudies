package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bo.AnchorDateTypeBo;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudyPermissionBO;
import com.fdahpstudydesigner.bo.StudySequenceBo;
import com.fdahpstudydesigner.dao.StudyDAO;
import com.fdahpstudydesigner.util.StudyExportSqlQueries;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
            StudyExportSqlQueries.STUDIES,
            studyBo.getId(),
            studyBo.getAppId(),
            studyBo.getCategory(),
            studyBo.getCreatedBy(),
            studyBo.getCreatedOn(),
            studyBo.getCustomStudyId(),
            studyBo.getDescription(),
            studyBo.getEnrollingParticipants(),
            studyBo.getFullName(),
            studyBo.getHasActivetaskDraft(),
            studyBo.getHasActivityDraft(),
            studyBo.getHasConsentDraft(),
            studyBo.getHasQuestionnaireDraft(),
            studyBo.getHasStudyDraft(),
            studyBo.getInboxEmailAddress(),
            studyBo.getIrbReview(),
            studyBo.getLive(),
            studyBo.getMediaLink(),
            studyBo.getModifiedBy(),
            studyBo.getModifiedOn(),
            studyBo.getName(),
            studyBo.getPlatform(),
            studyBo.getResearchSponsor(),
            studyBo.getSequenceNumber(),
            studyBo.getStatus(),
            studyBo.isStudyPreActiveFlag(),
            studyBo.getStudyTagLine(),
            studyBo.getStudyWebsite(),
            studyBo.getStudylunchDate(),
            studyBo.getTentativeDuration(),
            studyBo.getTentativeDurationWeekmonth(),
            studyBo.getThumbnailImage(),
            studyBo.getType(),
            studyBo.getVersion());

    StudyPermissionBO studyPermissionBo =
        studyDao.getStudyPermissionBO(studyBo.getId(), studyBo.getUserId());

    String studyPermissionsInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDY_PERMISSION,
            studyPermissionBo.getStudyPermissionId(),
            studyPermissionBo.getDelFlag(),
            studyPermissionBo.getProjectLead(),
            studyPermissionBo.getStudyId(),
            studyPermissionBo.getUserId(),
            studyPermissionBo.isViewPermission());

    StudySequenceBo studySequenceBo = studyDao.getStudySequenceByStudyId(studyBo.getId());

    String studySequeneInsertQuery =
        prepareInsertQuery(
            StudyExportSqlQueries.STUDY_SEQUENCE,
            studySequenceBo.getStudySequenceId(),
            studySequenceBo.isActions(),
            studySequenceBo.isBasicInfo(),
            studySequenceBo.isCheckList(),
            studySequenceBo.isComprehensionTest(),
            studySequenceBo.isConsentEduInfo(),
            studySequenceBo.iseConsent(),
            studySequenceBo.isEligibility(),
            studySequenceBo.isMiscellaneousBranding(),
            studySequenceBo.isMiscellaneousNotification(),
            studySequenceBo.isMiscellaneousResources(),
            studySequenceBo.isOverView(),
            studySequenceBo.isSettingAdmins(),
            studySequenceBo.isStudyDashboardChart(),
            studySequenceBo.isStudyDashboardStats(),
            studySequenceBo.isStudyExcActiveTask(),
            studySequenceBo.isStudyExcQuestionnaries(),
            studySequenceBo.getStudyId());

    AnchorDateTypeBo anchorDate = studyDao.getAnchorDateDetails(studyBo.getId());

    /*String anchorDateTypeInsertQuery =
    prepareInsertQuery(
        StudyExportSqlQueries.STUDY_SEQUENCE,
        studySequenceBo.getStudySequenceId(),
        studySequenceBo.isActions(),
        );*/

    insertSqlStatements.add(studiesInsertQuery);
    insertSqlStatements.add(studyPermissionsInsertQuery);
    insertSqlStatements.add(studySequeneInsertQuery);
  }

  private String prepareInsertQuery(String sqlQuery, Object... values) throws SQLException {
    Object[] columns =
        sqlQuery
            .substring(sqlQuery.indexOf('(') + 1, sqlQuery.indexOf(")"))
            .replace("`", "")
            .split(",");

    /* if (columns.length != values.length) {
      throw new SQLException("Column count doesn't match value count.");
    }*/

    int i = 0;
    for (Object column : columns) {
      if (values[i] instanceof Integer) {
        sqlQuery = sqlQuery.replaceFirst("<" + column + ">", (String) values[i]);
      } else {
        sqlQuery = sqlQuery.replaceFirst("<" + column + ">", "'" + values[i] + "'");
      }

      i++;
    }

    return sqlQuery;
  }

  public static void main(String[] args) throws SQLException {
    StudyExportService export = new StudyExportService();

    String query = export.prepareInsertQuery(StudyExportSqlQueries.STUDIES, "1");
    System.out.println(query);
  }
}
