package com.fdahpstudydesigner.util;

public class StudyExportSqlQueries {

  public static final String ACTIVETASK_SQL =
      "INSERT INTO `active_task` (`id`, `action`, `active`, `active_task_lifetime_end`, `active_task_lifetime_start`, `anchor_date_id`, `created_by`, `created_date`, `custom_study_id`, `day_of_the_week`, `display_name`, `duration`, `frequency`, `instruction`, `is_Change`, `is_live`, `modified_by`, `modified_date`, `repeat_active_task`, `schedule_type`, `short_title`, `study_id`, `task_type_id`, `task_title`, `version`) VALUES (<id>, <action>, <active>, <active_task_lifetime_end>, <active_task_lifetime_start>, <anchor_date_id>, <created_by>, <created_date>, <custom_study_id>, <day_of_the_week>, <display_name>, <duration>, <frequency>, <instruction>, <is_Change>, <is_live>, <modified_by>, <modified_date>, <repeat_active_task>, <schedule_type>, <short_title>, <study_id>, <task_type_id>, <task_title>, <version>);";

  public static final String ACTIVETASK_ATTRIBUTES_VALUES =
      "INSERT INTO `active_task_attrtibutes_values` (`active_task_attribute_id`, `active`, `active_task_id`, `active_task_master_attr_id`, `add_to_line_chart`, `attribute_val`, `display_name_stat`, `display_units_stat`, `formula_applied_stat`, `identifier_name_stat`, `rollback_chat`, `time_range_chart`, `time_range_stat`, `title_chat`, `upload_type_stat`, `use_for_statistic`) VALUES (<active_task_attribute_id>, <active>, <active_task_id>, <active_task_master_attr_id>, <add_to_line_chart>, <attribute_val>, <display_name_stat>, <display_units_stat>, <formula_applied_stat>, <identifier_name_stat>, <rollback_chat>, <time_range_chart>, <time_range_stat>, <title_chat>, <upload_type_stat>, <use_for_statistic>);";

  public static final String ACTIVETASK_CUSTOM_FREQUENCIES =
      "INSERT INTO `active_task_custom_frequencies` (`id`, `active_task_id`, `frequency_end_date`, `frequency_start_date`, `frequency_time`, `time_period_from_days`, `time_period_to_days`, `is_used`, `x_days_sign`, `y_days_sign`, `study_version`) VALUES (<id>, <active_task_id>, <frequency_end_date>, <frequency_start_date>, <frequency_time>, <time_period_from_days>, <time_period_to_days>, <is_used>, <x_days_sign>, <y_days_sign>, <study_version>);";

  public static final String ACTIVETASK_FREQUENCIES =
      "INSERT INTO `active_task_frequencies` (`id`, `active_task_id`, `frequency_date`, `frequency_time`, `is_launch_study`, `is_study_life_time`, `time_period_from_days`, `time_period_to_days`, `x_days_sign`, `y_days_sign`, `study_version`) VALUES (<id>, <active_task_id>, <frequency_date>, <frequency_time>, <is_launch_study>, <is_study_life_time>, <time_period_from_days>, <time_period_to_days>, <x_days_sign>, <y_days_sign>, <study_version>);";

  public static final String ACTIVETASK_LIST =
      "INSERT INTO `active_task_list` (`active_task_list_id`, `task_name`, `type`) VALUES (<active_task_list_id>, <task_name>, <type>);";

  public static final String ACTIVETASK_MASTER_ATTRIBUTE =
      "INSERT INTO `active_task_master_attribute` (`active_task_master_attr_id`, `add_to_dashboard`, `attribute_data_type`, `attribute_name`, `attribute_type`, `display_name`, `order_by`, `task_type_id`, `study_version`, `task_type`) VALUES (<active_task_master_attr_id>, <add_to_dashboard>, <attribute_data_type>, <attribute_name>, <attribute_type>, <display_name>, <order_by>, <task_type_id>, <study_version>, <task_type>);";

  public static final String ACTIVETASK_SELECT_OPTIONS =
      "INSERT INTO `active_task_select_options` (`active_task_select_options_id`, `active_task_master_attr_id`, `option_val`) VALUES (<active_task_select_options_id>, <active_task_master_attr_id>, <option_val>);";

  public static final String ACTIVE_TASK_STEPS =
      "INSERT INTO `active_task_steps` (`step_id`, `active_task_stepscol`, `active_task_id`, `sd_live_form_id`, `sequence_no`, `study_version`) VALUES (<step_id>, <active_task_stepscol>, <active_task_id>, <sd_live_form_id>, <sequence_no>, <study_version>);";

  public static final String ACTIVETASK_FORMULA =
      "INSERT INTO `activetask_formula` (`activetask_formula_id`, `value`, `formula`) VALUES (<activetask_formula_id>, <value>, <formula>);";

  public static final String ANCHORDATE_TYPE =
      "INSERT INTO `anchordate_type` (`id`, `custom_study_id`, `has_anchortype_draft`, `name`, `study_id`, `version`) VALUES (<id>, <custom_study_id>, <has_anchortype_draft>, <name>, <study_id>, <version>);";

  public static final String APP_VERSIONS =
      "INSERT INTO `app_versions` (`av_id`, `app_version`, `bundle_id`, `created_on`, `custom_study_id`, `force_update`, `message`, `os_type`) VALUES (<av_id>, <app_version>, <bundle_id>, <created_on>, <custom_study_id>, <force_update>, <message>, <os_type>);";

  public static final String AUDIT_LOG =
      "INSERT INTO `audit_log` (`audit_log_id`, `activity`, `activity_details`, `class_method_name`, `created_date_time`, `user_id`) VALUES (<audit_log_id>, <activity>, <activity_details>, <class_method_name>, <created_date_time>, <user_id>);";

  public static final String BRANDING =
      "INSERT INTO `branding` (`id`, `background`, `font`, `logo_image_path`, `study_id`, `tint`) VALUES (<id>, <background>, <font>, <logo_image_path>, <study_id>, <tint>);";

  public static final String CHARTS =
      "INSERT INTO `charts` (`id`, `allow_previous_next`, `chart_title`, `chart_type`, `reference_id`, `sequence_no`, `study_id`, `time_range`) VALUES (<id>, <allow_previous_next>, <chart_title>, <chart_type>, <reference_id>, <sequence_no>, <study_id>, <time_range>);";

  public static final String COMREHENSION_TEST_QUESTIONS =
      "INSERT INTO `comprehension_test_question` (`id`, `active`, `created_by`, `created_on`, `modified_by`, `modified_on`, `question_text`, `sequence_no`, `status`, `structure_of_correct_ans`, `study_id`, `study_version`) VALUES (<id>, <active>, <created_by>, <created_on>, <modified_by>, <modified_on>, <question_text>, <sequence_no>, <status>, <structure_of_correct_ans>, <study_id>, <study_version>);";

  public static final String COMREHENSION_TEST_RESPONSE =
      " INSERT INTO `comprehension_test_response` (`id`, `comprehension_test_question_id`, `correct_answer`, `response_option`, `study_version`) VALUES (<id>, <comprehension_test_question_id>, <correct_answer>, <response_option>, <study_version>);";

  public static final String CONSENT =
      "INSERT INTO `consent` (`id`, `allow_without_permission`, `comprehension_test_minimum_score`, `consent_doc_content`, `consent_doc_type`, `created_by`, `created_on`, `custom_study_id`, `e_consent_agree`, `e_consent_datetime`, `e_consent_firstname`, `e_consent_lastname`, `e_consent_signature`, `html_consent`, `learn_more_text`, `is_live`, `long_description`, `modified_by`, `modified_on`, `need_comprehension_test`, `share_data_permissions`, `short_description`, `study_id`, `tagline_description`, `title`, `version`, `enroll_again`) VALUES (<id>, <allow_without_permission>, <comprehension_test_minimum_score>, <consent_doc_content>, <consent_doc_type>, <created_by>, <created_on>, <custom_study_id>, <e_consent_agree>, <e_consent_datetime>, <e_consent_firstname>, <e_consent_lastname>, <e_consent_signature>, <html_consent>, <learn_more_text>, <is_live>, <long_description>, <modified_by>, <modified_on>, <need_comprehension_test>, <share_data_permissions>, <short_description>, <study_id>, <tagline_description>, <title>, <version>, <enroll_again>);";

  public static final String CONSENT_INFO =
      "INSERT INTO `consent_info` (`id`, `active`, `brief_summary`, `consent_item_title_id`, `consent_item_type`, `content_type`, `created_by`, `created_on`, `custom_study_id`, `display_title`, `elaborated`, `html_content`, `is_live`, `modified_by`, `modified_on`, `sequence_no`, `status`, `study_id`, `url`, `version`, `visual_step`) VALUES (<id>, <active>, <brief_summary>, <consent_item_title_id>, <consent_item_type>, <content_type>, <created_by>, <created_on>, <custom_study_id>, <display_title>, <elaborated>, <html_content>, <is_live>, <modified_by>, <modified_on>, <sequence_no>, <status>, <study_id>, <url>, <version>, <visual_step>);";

  public static final String CONSENT_MASTER_INFO =
      "INSERT INTO `consent_master_info` (`id`, `title`, `type`) VALUES (<id>, <title>, <type>);";

  public static final String ELIGIBILITY =
      "INSERT INTO `eligibility` (`id`, `created_by`, `created_on`, `eligibility_mechanism`, `failure_outcome_text`, `instructional_text`, `modified_by`, `modified_on`, `study_id`) VALUES (<id>, <created_by>, <created_on>, <eligibility_mechanism>, <failure_outcome_text>, <instructional_text>, <modified_by>, <modified_on>, <study_id>);";

  public static final String ELIGIBILITY_TEST =
      "INSERT INTO `eligibility_test` (`id`, `active`, `eligibility_id`, `question`, `response_format`, `response_no_option`, `response_yes_option`, `sequence_no`, `short_title`, `status`, `is_used`) VALUES (<id>, <active>, <eligibility_id>, <question>, <response_format>, <response_no_option>, <response_yes_option>, <sequence_no>, <short_title>, <status>, <is_used>);";

  public static final String ELIGIBILITY_TEST_RESPONSE =
      "INSERT INTO `eligibility_test_response` (`response_id`, `destination_question`, `eligibility_test_id`, `pass_fail`, `response_option`, `study_version`) VALUES (<response_id>, <destination_question>, <eligibility_test_id>, <pass_fail>, <response_option>, <study_version>);";

  public static final String ENROLLMENT_TOKEN =
      "INSERT INTO `enrollment_token` (`token_id`, `created_on`, `enrollment_token`) VALUES (<token_id>, <created_on>, <enrollment_token>);";

  public static final String FORM =
      "INSERT INTO `form` (`form_id`, `active`, `created_by`, `created_on`, `modified_by`, `modified_on`, `study_version`) VALUES (<form_id>, <active>, <created_by>, <created_on>, <modified_by>, <modified_on>, <study_version>);";

  public static final String FORM_MAPPING =
      "INSERT INTO `form_mapping` (`id`, `active`, `form_id`, `question_id`, `sequence_no`) VALUES (<id>, <active>, <form_id>, <question_id>, <sequence_no>);";

  public static final String GATEWAY_INFO =
      "INSERT INTO `gateway_info` (`id`, `email_inbox_address`, `fda_website_url`, `video_url`) VALUES (<id>, <email_inbox_address>, <fda_website_url>, <video_url>);";

  public static final String GATEWAY_WELCOME_INFO =
      "INSERT INTO `gateway_welcome_info` (`id`, `app_title`, `description`, `image_path`) VALUES (<id>, <app_title>, <description>, <image_path>);";

  public static final String HEALTHKIT_KEYS_INFO =
      "INSERT INTO `health_kit_keys_info` (`id`, `category`, `display_name`, `key_text`, `result_type`) VALUES (<id>, <category>, <display_name>, <key_text>, <result_type>);";

  public static final String INSTRUCTION =
      "INSERT INTO `instructions` (`id`, `active`, `created_by`, `created_on`, `instruction_text`, `instruction_title`, `modified_by`, `modified_on`, `status`) VALUES (<id>, <active>, <created_by>, <created_on>, <instruction_text>, <instruction_title>, <modified_by>, <modified_on>, <status>);";

  public static final String LEGAL_TEXT =
      "INSERT INTO `legal_text` (`id`, `mobile_app_privacy_policy`, `mobile_app_privacy_policy_modified_datetime`, `mobile_app_terms`, `mobile_app_terms_modified_datetime`, `web_app_privacy_policy`, `web_app_privacy_policy_modified_datetime`, `web_app_terms`, `web_app_terms_modified_datetime`) VALUES (<id>, <mobile_app_privacy_policy>, <mobile_app_privacy_policy_modified_datetime>, <mobile_app_terms>, <mobile_app_terms_modified_datetime>, <web_app_privacy_policy>, <web_app_privacy_policy_modified_datetime>, <web_app_terms>, <web_app_terms_modified_datetime>);";

  public static final String LINE_CHAER =
      "INSERT INTO `line_chart` (`id`, `animation_needed`, `line_chartcol`, `no_data_text`, `show_ver_hor_line`, `x_axis_color`, `y_axis_color`) VALUES (<id>, <animation_needed>, <line_chartcol>, <no_data_text>, <show_ver_hor_line>, <x_axis_color>, <y_axis_color>);";

  public static final String LINE_CHART_DATASOURCE =
      "INSERT INTO `line_chart_datasource` (`id`, `data_source_id`, `line_chart_id`, `plot_color`) VALUES (<id>, <data_source_id>, <line_chart_id>, <plot_color>);";

  public static final String LINE_CHART_X_AXIS =
      "INSERT INTO `line_chart_x_axis` (`id`, `line_chart_id`, `title`) VALUES (<id>, <line_chart_id>, <title>);";

  public static final String MASTER_DATA =
      "INSERT INTO `master_data` (`id`, `privacy_policy_text`, `terms_text`, `type`) VALUES (<id>, <privacy_policy_text>, <terms_text>, <type>);";

  public static final String NOTIFICATION =
      "INSERT INTO `notification` (`notification_id`, `active_task_id`, `is_anchor_date`, `app_id`, `created_by`, `created_on`, `custom_study_id`, `modified_by`, `modified_on`, `notification_action`, `notification_done`, `notification_schedule_type`, `notification_sent`, `notification_status`, `notification_subType`, `notification_text`, `notification_type`, `questionnarie_id`, `resource_id`, `schedule_date`, `schedule_time`, `study_id`, `x_days`, `schedule_timestamp`) VALUES (<notification_id>, <active_task_id>, <is_anchor_date>, <app_id>, <created_by>, <created_on>, <custom_study_id>, <modified_by>, <modified_on>, <notification_action>, <notification_done>, <notification_schedule_type>, <notification_sent>, <notification_status>, <notification_subType>, <notification_text>, <notification_type>, <questionnarie_id>, <resource_id>, <schedule_date>, <schedule_time>, <study_id>, <x_days>, <schedule_timestamp>);";

  public static final String NOTIFICATION_HISTORY =
      "INSERT INTO `notification_history` (`history_id`, `notification_id`, `notification_sent_date_time`) VALUES (<history_id>, <notification_id>, <notification_sent_date_time>);";

  public static final String QUESTION_CONDITION_BRANCHING =
      "INSERT INTO `question_condtion_branching` (`condition_id`, `active`, `input_type`, `input_type_value`, `parent_sequence_no`, `question_id`, `sequence_no`) VALUES (<condition_id>, <active>, <input_type>, <input_type_value>, <parent_sequence_no>, <question_id>, <sequence_no>);";

  public static final String QUESTIONNAIRES =
      "INSERT INTO `questionnaires` (`id`, `active`, `anchor_date_id`, `branching`, `created_by`, `created_date`, `custom_study_id`, `day_of_the_week`, `frequency`, `is_Change`, `is_live`, `modified_by`, `modified_date`, `repeat_questionnaire`, `schedule_type`, `short_title`, `status`, `study_id`, `study_lifetime_end`, `study_lifetime_start`, `title`, `version`) VALUES (<id>, <active>, <anchor_date_id>, <branching>, <created_by>, <created_date>, <custom_study_id>, <day_of_the_week>, <frequency>, <is_Change>, <is_live>, <modified_by>, <modified_date>, <repeat_questionnaire>, <schedule_type>, <short_title>, <status>, <study_id>, <study_lifetime_end>, <study_lifetime_start>, <title>, <version>);";

  public static final String QUESTIONNAIRES_CUSTOM_FREQUENCIES =
      "INSERT INTO `questionnaires_custom_frequencies` (`id`, `frequency_end_date`, `frequency_start_date`, `frequency_time`, `questionnaires_id`, `time_period_from_days`, `time_period_to_days`, `is_used`, `x_days_sign`, `y_days_sign`, `study_version`) VALUES (<id>, <frequency_end_date>, <frequency_start_date>, <frequency_time>, <questionnaires_id>, <time_period_from_days>, <time_period_to_days>, <is_used>, <x_days_sign>, <y_days_sign>, <study_version>);";

  public static final String QUESTIONNAIRES_FREQUENCIES =
      "INSERT INTO `questionnaires_frequencies` (`id`, `frequency_date`, `frequency_time`, `is_launch_study`, `is_study_life_time`, `questionnaires_id`, `time_period_from_days`, `time_period_to_days`, `x_days_sign`, `y_days_sign`) VALUES (<id>, <frequency_date>, <frequency_time>, <is_launch_study>, <is_study_life_time>, <questionnaires_id>, <time_period_from_days>, <time_period_to_days>, <x_days_sign>, <y_days_sign>);";

  public static final String QUESTIONNAIRES_STEPS =
      "INSERT INTO `questionnaires_steps` (`step_id`, `active`, `created_by`, `created_on`, `destination_step`, `instruction_form_id`, `modified_by`, `modified_on`, `questionnaires_id`, `repeatable`, `repeatable_text`, `sequence_no`, `skiappable`, `status`, `step_short_title`, `step_type`) VALUES (<step_id>, <active>, <created_by>, <created_on>, <destination_step>, <instruction_form_id>, <modified_by>, <modified_on>, <questionnaires_id>, <repeatable>, <repeatable_text>, <sequence_no>, <skiappable>, <status>, <step_short_title>, <step_type>);";

  public static final String QUESTIONS =
      "INSERT INTO `questions` (`id`, `active`, `add_line_chart`, `allow_healthkit`, `allow_rollback_chart`, `anchor_date_id`, `chart_title`, `created_by`, `created_on`, `description`, `healthkit_datatype`, `line_chart_timerange`, `modified_by`, `modified_on`, `question`, `response_type`, `short_title`, `skippable`, `stat_display_name`, `stat_diaplay_units`, `stat_formula`, `stat_short_name`, `stat_type`, `status`, `use_anchor_date`, `use_stastic_data`) VALUES (<id>, <active>, <add_line_chart>, <allow_healthkit>, <allow_rollback_chart>, <anchor_date_id>, <chart_title>, <created_by>, <created_on>, <description>, <healthkit_datatype>, <line_chart_timerange>, <modified_by>, <modified_on>, <question>, <response_type>, <short_title>, <skippable>, <stat_display_name>, <stat_diaplay_units>, <stat_formula>, <stat_short_name>, <stat_type>, <status>, <use_anchor_date>, <use_stastic_data>);";

  public static final String QUESTIONS_RESPONSE_TYPE =
      "INSERT INTO `questions_response_type` (`id`, `parameter_name`, `parameter_value`, `question_id`, `study_version`) VALUES (<id>, <parameter_name>, <parameter_value>, <question_id>, <study_version>);";

  public static final String REFERENCE_TABLES =
      "INSERT INTO `reference_tables` (`id`, `category`, `type`, `str_value`) VALUES (<id>, <category>, <type>, <str_value>);";

  public static final String RESOURCES =
      "INSERT INTO `resources` (`id`, `action`, `anchor_date_id`, `created_by`, `created_on`, `end_date`, `modified_by`, `modified_on`, `pdf_name`, `pdf_url`, `resource_text`, `resource_type`, `resource_visibility`, `rich_text`, `sequence_no`, `start_date`, `status`, `study_id`, `study_protocol`, `text_or_pdf`, `time_period_from_days`, `time_period_to_days`, `title`, `x_days_sign`, `y_days_sign`) VALUES (<id>, <action>, <anchor_date_id>, <created_by>, <created_on>, <end_date>, <modified_by>, <modified_on>, <pdf_name>, <pdf_url>, <resource_text>, <resource_type>, <resource_visibility>, <rich_text>, <sequence_no>, <start_date>, <status>, <study_id>, <study_protocol>, <text_or_pdf>, <time_period_from_days>, <time_period_to_days>, <title>, <x_days_sign>, <y_days_sign>);";

  public static final String RESPONSE_SUB_TYPE_VALUE =
      "INSERT INTO `response_sub_type_value` (`response_sub_type_value_id`, `active`, `description`, `destination_step_id`, `detail`, `exclusive`, `image`, `response_type_id`, `selected_image`, `text`, `value`, `operator`, `value_of_x`) VALUES (<response_sub_type_value_id>, <active>, <description>, <destination_step_id>, <detail>, <exclusive>, <image>, <response_type_id>, <selected_image>, <study_version>, <text>, <value>, <operator>, <value_of_x>);";

  public static final String RESPONSE_TYPE_VALUE =
      "INSERT INTO `response_type_value` (`response_type_id`, `active`, `condition_formula`, `default_date`, `defalut_time`, `default_value`, `formula_based_logic`, `image_size`, `invalid_message`, `max_date`, `max_desc`, `max_fraction_digits`, `max_image`, `max_length`, `max_value`, `measurement_system`, `min_date`, `min_desc`, `min_image`, `min_value`, `multiple_lines`, `other_description`, `other_destination_step_id`, `other_exclusive`, `other_include_text`, `other_participant_fill`, `other_placeholder_text`, `other_text`, `other_type`, `other_value`, `placeholder`, `questions_response_type_id`, `selection_style`, `step`, `style`, `text_choices`, `unit`, `use_current_location`, `validation_characters`, `validation_condition`, `validation_except_text`, `validation_regex`, `vertical`, `study_version`) VALUES (<response_type_id>, <active>, <condition_formula>, <default_date>, <defalut_time>, <default_value>, <formula_based_logic>, <image_size>, <invalid_message>, <max_date>, <max_desc>, <max_fraction_digits>, <max_image>, <max_length>, <max_value>, <measurement_system>, <min_date>, <min_desc>, <min_image>, <min_value>, <multiple_lines>, <other_description>, <other_destination_step_id>, <other_exclusive>, <other_include_text>, <other_participant_fill>, <other_placeholder_text>, <other_text>, <other_type>, <other_value>, <placeholder>, <questions_response_type_id>, <selection_style>, <step>, <style>, <text_choices>, <unit>, <use_current_location>, <validation_characters>, <validation_condition>, <validation_except_text>, <validation_regex>, <vertical>, <study_version>);";

  public static final String ROLES =
      "INSERT INTO `roles` (`role_id`, `role_name`) VALUES (<role_id>, <role_name>);";

  public static final String STATISTIC_MASTER_IMAGES =
      "INSERT INTO `statistic_master_images` (`statistic_image_id`, `value`) VALUES (<statistic_image_id>, <value>);";

  public static final String STATISTICS =
      "INSERT INTO `statistics` (`id`, `custom`, `custom_end`, `custom_start`, `data_source`, `display_name`, `display_unit`, `formula`, `short_title`, `stat_type`, `time_range`) VALUES (<id>, <custom>, <custom_end>, <custom_start>, <data_source>, <display_name>, <display_unit>, <formula>, <short_title>, <stat_type>, <time_range>);";

  public static final String STUDIES =
      "INSERT INTO `studies` (`id`, `app_id`, `category`, `created_by`, `created_on`, `custom_study_id`, `description`, `enrolling_participants`,  `full_name`, `has_activitetask_draft`, `has_activity_draft`, `has_consent_draft`, `has_questionnaire_draft`, `has_study_draft`, `inbox_email_address`, `irb_review`, `is_live`, `media_link`, `modified_by`, `modified_on`, `name`, `platform`, `research_sponsor`, `sequence_number`, `status`, `study_pre_active_flag`, `study_tagline`, `study_website`, `study_lunched_date`, `tentative_duration`, `tentative_duration_weekmonth`, `thumbnail_image`, `type`, `version`) VALUES (<id>, <app_id>, <category>, <created_by>, <created_on>, <custom_study_id>, <description>, <enrolling_participants>, <full_name>, <has_activitetask_draft>, <has_activity_draft>, <has_consent_draft>, <has_questionnaire_draft>, <has_study_draft>, <inbox_email_address>, <irb_review>, <is_live>, <media_link>, <modified_by>, <modified_on>, <name>, <platform>, <research_sponsor>, <sequence_number>, <status>, <study_pre_active_flag>, <study_tagline>, <study_website>, <study_lunched_date>, <tentative_duration>, <tentative_duration_weekmonth>, <thumbnail_image>, <type>, <version>);";

  public static final String STUDY_ACTIVITY_VERSION =
      "INSERT INTO `study_activity_version` (`study_activity_id`, `activity_id`, `activity_type`, `activity_version`, `custom_study_id`, `short_title`, `study_version`) VALUES (<study_activity_id>, <activity_id>, <activity_type>, <activity_version>, <custom_study_id>, <short_title>, <study_version>);";

  public static final String STUDY_CHECKLIST =
      "INSERT INTO `study_checklist` (`checklist_id`, `checkbox1`, `checkbox10`, `checkbox11`, `checkbox12`, `checkbox2`, `checkbox3`, `checkbox4`, `checkbox5`, `checkbox6`, `checkbox7`, `checkbox8`, `checkbox9`, `created_by`, `created_on`, `custom_study_id`, `modified_by`, `modified_on`, `study_id`) VALUES (<checklist_id>, <checkbox1>, <checkbox10>, <checkbox11>, <checkbox12>, <checkbox2>, <checkbox3>, <checkbox4>, <checkbox5>, <checkbox6>, <checkbox7>, <checkbox8>, <checkbox9>, <created_by>, <created_on>, <custom_study_id>, <modified_by>, <modified_on>, <study_id>);";

  public static final String STUDY_PAGE =
      "INSERT INTO `study_page` (`page_id`, `created_by`, `created_on`, `description`, `image_path`, `modified_by`, `modified_on`, `study_id`, `title`) VALUES (<page_id>, <created_by>, <created_on>, <description>, <image_path>, <modified_by>, <modified_on>, <study_id>, <title>);";

  public static final String STUDY_PERMISSION =
      "INSERT INTO `study_permission` (`id`, `delFlag`, `project_lead`, `study_id`, `user_id`, `view_permission`) VALUES (<id>, <delFlag>, <project_lead>, <study_id>, <user_id>, <view_permission>);";

  public static final String STUDY_SEQUENCE =
      "INSERT INTO `study_sequence` (`study_sequence_id`, `actions`, `basic_info`, `check_list`, `comprehension_test`, `consent_edu_info`, `e_consent`, `eligibility`, `miscellaneous_branding`, `miscellaneous_notification`, `miscellaneous_resources`, `over_view`, `setting_admins`, `study_dashboard_chart`, `study_dashboard_stats`, `study_exc_active_task`, `study_exc_questionnaries`, `study_id`) VALUES (<study_sequence_id>, <actions>, <basic_info>, <check_list>, <comprehension_test>, <consent_edu_info>, <e_consent>, <eligibility>, <miscellaneous_branding>, <miscellaneous_notification>, <miscellaneous_resources>, <over_view>, <setting_admins>, <study_dashboard_chart>, <study_dashboard_stats>, <study_exc_active_task>, <study_exc_questionnaries>, <study_id>);";

  public static final String STUDY_VERSION =
      "INSERT INTO `study_version` (`version_id`, `activity_version`, `consent_version`, `custom_study_id`, `study_version`) VALUES (<version_id>, <activity_version>, <consent_version>, <custom_study_id>, <study_version>);";

  public static final String VERSION_INFO =
      "INSERT INTO `version_info` (`version_info_id`, `android_force_update`, `android`, `app_id`, `ios_force_update`, `ios`) VALUES (<version_info_id>, <android_force_update>, <android>, <app_id>, <ios_force_update>, <ios>);";
}
