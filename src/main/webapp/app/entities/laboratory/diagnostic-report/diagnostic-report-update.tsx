import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { translate, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, updateEntity } from './diagnostic-report.reducer';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import { Button, Divider, Form, Space, Typography } from 'antd';
import DiagnosticReportUpdateField from 'app/entities/laboratory/diagnostic-report/diagnostic-report-update-field';
import DiagnosticReportPatientDescriptions from 'app/entities/laboratory/diagnostic-report/diagnostic-report-patient-descriptions';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { LeftOutlined } from '@ant-design/icons';
import { useForm } from 'antd/es/form/Form';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';

export const DiagnosticReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id, diagnosticReportId } = useParams();

  const isNew = diagnosticReportId === undefined;

  const diagnosticReportEntity: IDiagnosticReport = useAppSelector(state => state.laboratory.diagnosticReport.entity);
  const loading = useAppSelector(state => state.laboratory.diagnosticReport.loading);
  const updating = useAppSelector(state => state.laboratory.diagnosticReport.updating);
  const updateSuccess = useAppSelector(state => state.laboratory.diagnosticReport.updateSuccess);
  const [form] = useForm();

  const handleClose = () => {
    navigate(`/laboratory/service-request/${id}`);
  };

  useEffect(() => {
    if (isNew) {
      navigate(`/laboratory/service-request/${id}`);
    } else {
      dispatch(getEntity(diagnosticReportId));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...diagnosticReportEntity,
      fields: Object.keys(values)?.map(el => {
        return { key: el, value: values[el] };
      }),
    };
    dispatch(updateEntity(entity));
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...diagnosticReportEntity,
          subject: diagnosticReportEntity?.subject?.id,
          format: diagnosticReportEntity?.format?.id,
          basedOn: diagnosticReportEntity?.basedOn?.id,
        };

  if (diagnosticReportEntity && DiagnosticReportStatus[diagnosticReportEntity.status] !== DiagnosticReportStatus.REGISTERED) {
    navigate(`/laboratory/service-request/${id}`);
  }

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
      <Typography.Title level={2} style={{ textAlign: 'center' }}>
        {diagnosticReportEntity?.format?.name}
      </Typography.Title>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <Space direction={'vertical'} style={{ width: '100%' }}>
          <DiagnosticReportPatientDescriptions subject={diagnosticReportEntity?.subject} />
          <Divider />
          <Form onFinish={saveEntity} initialValues={defaultValues()} style={{ width: '100%' }}>
            {diagnosticReportEntity?.format?.fieldFormats?.map((el, index) => (
              <DiagnosticReportUpdateField key={index} el={el} index={index} form={form} />
            ))}
            <Button type="primary" id="save-entity" data-cy="entityCreateSaveButton" htmlType="submit" disabled={updating}>
              <FontAwesomeIcon icon="save" />
              &nbsp;
              <Translate contentKey="entity.action.save">Save</Translate>
            </Button>
          </Form>
        </Space>
      )}
    </>
  );
};

export default DiagnosticReportUpdate;
