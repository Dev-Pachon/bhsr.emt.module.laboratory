import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Form, Input, Modal, Select, Transfer, Typography } from 'antd';
import { translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity as createServiceRequest, reset as resetServiceRequest } from './service-request.reducer';
import { getEntities as getFormats } from '../diagnostic-report-format/diagnostic-report-format.reducer';
import { useForm } from 'antd/es/form/Form';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { IServiceRequest } from 'app/shared/model/laboratory/service-request.model';
import { ServiceRequestPriority } from 'app/shared/model/enumerations/service-request-priority.model';
import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';

const { Title } = Typography;

interface TransferItem {
  key: string;
  title: string;
  description?: string;
  disabled?: boolean;
}

interface ServiceRequestModalProps {
  patient: IPatient;
}

const ServiceRequestModal: React.FC<ServiceRequestModalProps> = ({ patient, ...props }) => {
  const [open, setOpen] = useState(false);
  const [confirmLoading, setConfirmLoading] = useState(false);
  const [form] = useForm<IServiceRequest>();
  const [diagnosticReportFormatOptions, setDiagnosticReportFormatOptions] = useState<TransferItem[]>([]);
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const serviceRequestEntity = useAppSelector(state => state.laboratory.serviceRequest.entity);
  const diagnosticReportFormatEntity = useAppSelector(state => state.laboratory.diagnosticReportFormat.entities);
  const loadingFormats = useAppSelector(state => state.laboratory.diagnosticReportFormat.loading);

  const updateSuccess = useAppSelector(state => state.laboratory.serviceRequest.updateSuccess);

  useEffect(() => {
    dispatch(resetServiceRequest());
    dispatch(getFormats({}));
  }, []);

  useEffect(() => {
    if (diagnosticReportFormatEntity && diagnosticReportFormatEntity.length > 0) {
      setDiagnosticReportFormatOptions(
        diagnosticReportFormatEntity.map((item: IDiagnosticReportFormat, idx) => {
          return { key: idx, title: item.name };
        })
      );
    }
  }, [diagnosticReportFormatEntity]);

  useEffect(() => {
    if (updateSuccess) {
      setConfirmLoading(false);
      handleCancel();
    } else if (!confirmLoading) {
      console.log('error');
    }
  }, [updateSuccess]);

  const saveEntity = (values: IServiceRequest) => {
    console.log(values);
    const entity = {
      ...serviceRequestEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createServiceRequest(entity));
    }
  };

  const showModal = () => {
    setOpen(true);
  };

  const handleOk = () => {
    setConfirmLoading(true);
    console.log(form.getFieldsValue());
    form
      .validateFields()
      .then(values => {
        form.resetFields();

        const valuesToSave = { ...values };

        valuesToSave.diagnosticReportsFormats = values.diagnosticReportsFormats.map(item => diagnosticReportFormatEntity[item]);

        saveEntity(valuesToSave);
        setOpen(false);
        setConfirmLoading(false);
        dispatch(resetServiceRequest());
        form.resetFields();
      })
      .catch(info => {
        console.log('Validate Failed:', info);
        setConfirmLoading(false);
      });
  };

  const handleCancel = () => {
    setOpen(false);
    form.resetFields();
  };

  const defaultValues = { status: 'DRAFT', subject: patient.id, category: 'LABORATORY_PROCEDURE' };

  const filterOption = (input: string, option?: { label: string; value: string }) =>
    (option?.label ?? '').toLowerCase().includes(input.toLowerCase());

  return (
    <>
      {patient === undefined ? null : (
        <>
          <Button type="primary" onClick={showModal}>
            Request a Service
          </Button>
          <Modal
            title={translate('laboratoryApp.laboratoryServiceRequest.home.createLabel')}
            open={open}
            onOk={handleOk}
            onCancel={handleCancel}
            footer={[
              <Button key="back" onClick={handleCancel}>
                Cancel
              </Button>,
              <Button key="submit" type="primary" loading={confirmLoading} onClick={handleOk}>
                Request Service
              </Button>,
            ]}
          >
            <Form form={form} initialValues={defaultValues} onFinish={saveEntity} layout={'vertical'} disabled={confirmLoading}>
              <Title level={4}>Patient: {patient.name.text}</Title>

              <Form.Item<IServiceRequest>
                label={translate('laboratoryApp.laboratoryServiceRequest.patient')}
                id="service-request-patient"
                name="subject"
                data-cy="patient"
                hidden
              >
                <Input readOnly />
              </Form.Item>

              <Form.Item<IServiceRequest>
                label={translate('laboratoryApp.laboratoryServiceRequest.status')}
                id="service-request-status"
                name="status"
                data-cy="status"
                hidden
              >
                <Input readOnly hidden />
              </Form.Item>

              <Form.Item<IServiceRequest>
                label={translate('laboratoryApp.laboratoryServiceRequest.category')}
                id="service-request-category"
                name="category"
                data-cy="category"
                hidden
              >
                <Input readOnly />
              </Form.Item>

              <Form.Item<IServiceRequest>
                name="priority"
                label={translate('laboratoryApp.laboratoryServiceRequest.create.priority.label')}
                rules={[{ required: true }]}
              >
                <Select
                  showSearch
                  placeholder="Select a priority"
                  optionFilterProp="children"
                  filterOption={filterOption}
                  options={Object.keys(ServiceRequestPriority).map((key: string) => {
                    return {
                      value: key,
                      label: translate(`laboratoryApp.laboratoryServiceRequest.create.priority.${key}`),
                    };
                  })}
                />
              </Form.Item>

              <Form.Item<IServiceRequest>
                name="diagnosticReportsFormats"
                label={translate('laboratoryApp.laboratoryServiceRequest.create.diagnosticReportIds')}
                valuePropName={'targetKeys'}
                rules={[{ required: true }]}
              >
                <Transfer
                  titles={['Available', 'To be Requested']}
                  dataSource={diagnosticReportFormatOptions}
                  showSearch
                  oneWay
                  render={item => item.title}
                />
              </Form.Item>
            </Form>
          </Modal>
        </>
      )}
    </>
  );
};

export default ServiceRequestModal;
