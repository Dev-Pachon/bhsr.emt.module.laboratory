import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { TextFormat, translate, Translate } from 'react-jhipster';
import type { ColumnsType } from 'antd/es/table';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-request.reducer';
import { IServiceRequestResponse } from 'app/shared/model/laboratory/service-request.model';
import { Descriptions, Space, Table } from 'antd';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { LeftOutlined } from '@ant-design/icons';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { DiagnosticReportStatus } from 'app/shared/model/enumerations/diagnostic-report-status.model';

export const ServiceRequestDetail = () => {
  const dispatch = useAppDispatch();
  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB]));
  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceRequestEntity = useAppSelector(state => state.laboratory.serviceRequest.entity);

  const columns: ColumnsType<IDiagnosticReport> = [
    {
      title: 'Nombre',
      dataIndex: 'format',
      key: 'format',
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
    {
      title: 'Acción',
      dataIndex: '',
      key: 'x',
      render: (text, record) => (
        <Space size="middle">
          <Link to={`diagnostic-report/${record.id}`}>
            <Translate contentKey="entity.action.view">View</Translate>
          </Link>
          {isLabUser &&
          serviceRequestEntity &&
          ServiceRequestStatus[serviceRequestEntity.status] === ServiceRequestStatus.ACTIVE &&
          DiagnosticReportStatus[record.status] !== DiagnosticReportStatus.FINAL ? (
            <Link to={`diagnostic-report/${record.id}/edit`}>
              <Translate contentKey="entity.action.complete">Complete</Translate>
            </Link>
          ) : null}
        </Space>
      ),
    },
  ];

  const mapperToDescriptionItem = (serviceRequest: IServiceRequestResponse) => {
    const items = [
      {
        key: 'patientId',
        label: 'Patient',
        children: serviceRequest.subject?.name?.text,
      },
      {
        key: 'diagnosticReports',
        label: '# Diagnostic Reports',
        children: serviceRequest.diagnosticReports?.length,
      },
      {
        key: 'status',
        label: 'Status',
        children: translate(`laboratoryApp.ServiceRequestStatus.${serviceRequest.status}`),
      },
      {
        key: 'priority',
        label: 'Priority',
        children: translate(`laboratoryApp.laboratoryServiceRequest.create.priority.${serviceRequest.priority}`),
      },
      {
        key: 'createdBy',
        label: 'Created by',
        children: `${serviceRequest?.createdBy?.firstName} ${serviceRequest?.createdBy?.lastName}`,
      },
      {
        key: 'createdAt',
        label: 'Created at',
        children: <TextFormat value={serviceRequest.createdAt} type="date" format={APP_DATE_FORMAT} />,
      },
      {
        key: 'updatedBy',
        label: 'Last update by',
        children: `${serviceRequest?.updatedBy?.firstName} ${serviceRequest?.updatedBy?.lastName}`,
      },
      {
        key: 'updateAt',
        label: 'Last update at',
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
