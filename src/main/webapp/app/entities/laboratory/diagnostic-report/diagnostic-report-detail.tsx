import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report.reducer';
import { Descriptions, Divider, Space } from 'antd';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import DiagnosticReportPatientDescriptions from 'app/entities/laboratory/diagnostic-report/diagnostic-report-patient-descriptions';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { LeftOutlined } from '@ant-design/icons';

export const DiagnosticReportDetail = () => {
  const dispatch = useAppDispatch();

  const { diagnosticReportId, id } = useParams();

  useEffect(() => {
    dispatch(getEntity(diagnosticReportId));
  }, []);

  const diagnosticReportEntity = useAppSelector(state => state.laboratory.diagnosticReport.entity);

  const getItems = (diagnosticReport: IDiagnosticReport) => {
    const { format } = diagnosticReport;

    if (diagnosticReport.fields) {
      return diagnosticReport?.fields?.map((el, index) => {
        return {
          key: index,
          label: el?.key,
          children: `${el?.value || ''}`,
        };
      });
    } else {
      return format?.fieldFormats?.map((el, index) => {
        return {
          key: index,
          label: el.name,
          children: ``,
        };
      });
    }
  };

  return (
    <>
      <PageHeader
        title={translate('laboratoryApp.laboratoryDiagnosticReport.detail.title')}
        leftAction={
          <Link to={`/laboratory/service-request/${id}`}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Space direction={'vertical'} style={{ width: '100%' }}>
        <DiagnosticReportPatientDescriptions subject={diagnosticReportEntity?.subject} />
        <Divider />
        <Descriptions title={diagnosticReportEntity?.format?.name} bordered items={getItems(diagnosticReportEntity)} column={4} />
      </Space>
    </>
  );
};

export default DiagnosticReportDetail;
