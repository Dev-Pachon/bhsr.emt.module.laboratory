import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { TextFormat, translate } from 'react-jhipster';
import type { ColumnsType } from 'antd/es/table';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-request.reducer';
import { IServiceRequestResponse } from 'app/shared/model/laboratory/service-request.model';
import { Descriptions, Space, Table, Tag } from 'antd';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { IDiagnosticReport, IDiagnosticReportLight } from 'app/shared/model/laboratory/diagnostic-report.model';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { LeftOutlined } from '@ant-design/icons';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';
import { ServiceRequestPriority } from 'app/shared/model/enumerations/service-request-priority.model';
import { Link as MUILink } from '@mui/material';

import { FabButton } from 'app/entities/laboratory/shared/fab-button';
import { Start } from '@mui/icons-material';

const getTagColor = serviceRequestResponse => {
  switch (ServiceRequestPriority[serviceRequestResponse.priority]) {
    case ServiceRequestPriority.HIGH:
      return 'error';
    case ServiceRequestPriority.MEDIUM:
      return 'warning';
    case ServiceRequestPriority.LOW:
      return 'processing';
    default:
      return 'processing';
  }
};
export const ServiceRequestDetail = () => {
  const dispatch = useAppDispatch();
  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB_USER]));
  const { id } = useParams<'id'>();
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceRequestEntity = useAppSelector(state => state.laboratory.serviceRequest.entity);

  const columns: ColumnsType<IDiagnosticReportLight> = [
    {
      title: '#',
      dataIndex: '',
      key: 'index',
      render: (text, record, index) => index + 1,
    },
    {
      title: 'Nombre',
      dataIndex: 'format',
      key: 'format',
      render: (text, record) => (
        <MUILink component={Link} to={`diagnostic-report/${record.id}`}>
          {record.format}
        </MUILink>
      ),
    },
    {
      title: 'Estado',
      dataIndex: 'status',
      key: 'status',
      render: (text, record) => translate(`laboratoryApp.DiagnosticReportStatus.${record.status}`),
    },
    {
      title: 'Creado en',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (text, record) => <TextFormat value={record.createdAt} type="date" format={APP_DATE_FORMAT} />,
    },
    {
      title: 'Creado por',
      dataIndex: 'createdBy',
      key: 'createdBy',
      render: (text, record) => `${record?.createdBy?.firstName} ${record?.createdBy?.lastName}`,
    },
    {
      title: 'Actualizado en',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      render: (text, record) => <TextFormat value={record.updatedAt} type="date" format={APP_DATE_FORMAT} />,
    },
    {
      title: 'Actualizado por',
      dataIndex: 'updatedBy',
      key: 'updatedBy',
      render: (text, record) => `${record?.updatedBy?.firstName} ${record?.updatedBy?.lastName}`,
    },
  ];

  if (isLabUser) {
    columns.push({
      title: 'Acción',
      dataIndex: '',
      key: 'x',
      render: (text, record) => (
        <Space size="middle">
          {isLabUser &&
            serviceRequestEntity &&
            ServiceRequestStatus[serviceRequestEntity.status] === ServiceRequestStatus.ACTIVE &&
            DiagnosticReportStatus[record.status] !== DiagnosticReportStatus.FINAL && (
              <FabButton Icon={Start} onClick={() => handleStartRequest(record.id)} color={'info'} />
            )}
        </Space>
      ),
    });
  }

  const handleStartRequest = paramId => {
    navigate(`diagnostic-report/${paramId}/edit`);
  };

  const mapperToDescriptionItem = (serviceRequest: IServiceRequestResponse) => {
    const items = [
      {
        key: 'patientId',
        label: 'Paciente',
        children: serviceRequest.subject?.name?.text,
      },
      {
        key: 'diagnosticReports',
        label: '# Informes de diagnóstico',
        children: serviceRequest.diagnosticReports?.length,
      },
      {
        key: 'status',
        label: 'Estado',
        children: translate(`laboratoryApp.ServiceRequestStatus.${serviceRequest.status}`),
      },
      {
        key: 'priority',
        label: 'Prioridad',
        children: (
          <Tag color={getTagColor(serviceRequest)}>
            {translate(`laboratoryApp.laboratoryServiceRequest.create.priority.${serviceRequest.priority}`)}
          </Tag>
        ),
      },
      {
        key: 'createdBy',
        label: 'Creado por',
        children: `${serviceRequest?.createdBy?.firstName} ${serviceRequest?.createdBy?.lastName}`,
      },
      {
        key: 'createdAt',
        label: 'Creado en',
        children: <TextFormat value={serviceRequest.createdAt} type="date" format={APP_DATE_FORMAT} />,
      },
      {
        key: 'updatedBy',
        label: 'Actualizado por',
        children: `${serviceRequest?.updatedBy?.firstName} ${serviceRequest?.updatedBy?.lastName}`,
      },
      {
        key: 'updateAt',
        label: 'Actualizado en',
        children: <TextFormat value={serviceRequest.updatedAt} type="date" format={APP_DATE_FORMAT} />,
      },
    ];
    return items;
  };

  return (
    <>
      <PageHeader
        title={translate('laboratoryApp.laboratoryServiceRequest.detail.title')}
        leftAction={
          <Link to={`/laboratory/service-request`}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Space size={'middle'} direction={'vertical'} style={{ width: '100%' }}>
        <Descriptions bordered size={'small'} items={mapperToDescriptionItem(serviceRequestEntity)} column={4} />
        <Table columns={columns} dataSource={serviceRequestEntity?.diagnosticReports} rowKey={r => r.id} />
      </Space>
    </>
  );
};

export default ServiceRequestDetail;
