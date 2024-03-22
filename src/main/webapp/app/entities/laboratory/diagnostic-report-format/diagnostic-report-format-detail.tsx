import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report-format.reducer';
import { IFieldFormat } from 'app/shared/model/laboratory/field-format.model';
import { Descriptions, Typography } from 'antd';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { LeftOutlined } from '@ant-design/icons';

export const DiagnosticReportFormatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entity);
  return (
    <>
      <PageHeader
        title={translate('laboratoryApp.laboratoryDiagnosticReportFormat.detail.title')}
        leftAction={
          <Link to={`/laboratory/diagnostic-report-format`} style={{ placeSelf: 'end' }}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Descriptions
        title={<Typography.Title level={4}>{diagnosticReportFormatEntity.name}</Typography.Title>}
        bordered
        items={diagnosticReportFormatEntity?.fieldFormats?.map((e: IFieldFormat) => ({
          key: e.id,
          label: e.name,
          children: (
            <>
              <Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.dataType')}: ${translate(
                `laboratoryApp.fieldFormatType.${e.dataType}`
              )}`}</Typography>
              {e.valueSet ? (
                <Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.hasValueSet')}: ${
                  e.valueSet ? translate('global.true') : translate('global.false')
                }`}</Typography>
              ) : null}
              {/*<Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.defaultValue')}: ${*/}
              {/*  e.defaultValue*/}
              {/*}`}</Typography>*/}
              {e.referenceValue ? (
                <Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.referenceValue')}: ${
                  e.referenceValue
                }`}</Typography>
              ) : null}
              <Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.isRequired')}: ${
                e.isRequired ? translate('global.true') : translate('global.false')
              }`}</Typography>
              <Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.isSearchable')}: ${
                e.isSearchable ? translate('global.true') : translate('global.false')
              }`}</Typography>
            </>
          ),
        }))}
      />
    </>
  );
};

export default DiagnosticReportFormatDetail;
