import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, translate, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import type { ColumnsType } from 'antd/es/table';

import { APP_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service-request.reducer';
import { IServiceRequestResponse } from 'app/shared/model/laboratory/service-request.model';
import { Descriptions, Space, Table } from 'antd';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';

export const ServiceRequestDetail = () => {
  const dispatch = useAppDispatch();
  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB]));

  const columns: ColumnsType<IDiagnosticReport> = [
    {
      title: 'Name',
      dataIndex: 'format',
      key: 'format',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (text, record) => translate(`laboratoryApp.DiagnosticReportStatus.${record.status}`),
    },
    {
      title: 'Created At',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (text, record) => <TextFormat value={record.createdAt} type="date" format={APP_DATE_FORMAT} />,
    },
    {
      title: 'Created By',
      dataIndex: 'createdBy',
      key: 'createdBy',
      render: (text, record) => `${record?.createdBy?.firstName} ${record?.createdBy?.lastName}`,
    },
    {
      title: 'Updated At',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      render: (text, record) => <TextFormat value={record.updatedAt} type="date" format={APP_DATE_FORMAT} />,
    },
    {
      title: 'Updated By',
      dataIndex: 'updatedBy',
      key: 'updatedBy',
      render: (text, record) => `${record?.updatedBy?.firstName} ${record?.updatedBy?.lastName}`,
    },
    {
      title: 'Action',
      dataIndex: '',
      key: 'x',
      render: (text, record) => (
        <Space size="middle">
          <Link to={`diagnostic-report/${record.id}`}>
            <Translate contentKey="entity.action.view">View</Translate>
          </Link>
          {isLabUser ? (
            <Link to={`diagnostic-report/${record.id}/edit`}>
              <Translate contentKey="entity.action.complete">Complete</Translate>
            </Link>
          ) : null}
        </Space>
      ),
    },
  ];

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceRequestEntity = useAppSelector(state => state.laboratory.serviceRequest.entity);

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
    <Row>
      <Col>
        <Space size={'middle'} direction={'vertical'} style={{ width: '100%' }}>
          <h2 data-cy="serviceRequestDetailsHeading">
            <Translate contentKey="laboratoryApp.laboratoryServiceRequest.detail.title">ServiceRequest</Translate>
          </h2>
          <Descriptions bordered size={'small'} items={mapperToDescriptionItem(serviceRequestEntity)} column={4} />
          <Table columns={columns} dataSource={serviceRequestEntity?.diagnosticReports} rowKey={r => r.id} />

          <Space>
            <Link to={`/laboratory/service-request`}>
              <FontAwesomeIcon icon="arrow-left" /> <Translate contentKey="entity.action.back">Back</Translate>
            </Link>
          </Space>
        </Space>
      </Col>
    </Row>
  );
};

export default ServiceRequestDetail;
