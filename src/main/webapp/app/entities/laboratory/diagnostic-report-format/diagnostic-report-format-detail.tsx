import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './diagnostic-report-format.reducer';
import { IFieldFormat } from 'app/shared/model/laboratory/field-format.model';
import { Descriptions, Typography } from 'antd';

export const DiagnosticReportFormatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entity);
  return (
    <Row>
      <Col md="8">
        <Descriptions
          title={diagnosticReportFormatEntity.name}
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
                <Typography>{`${translate('laboratoryApp.laboratoryDiagnosticReportFormat.fields.defaultValue')}: ${
                  e.defaultValue
                }`}</Typography>
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
      </Col>
    </Row>
  );
};

export default DiagnosticReportFormatDetail;
