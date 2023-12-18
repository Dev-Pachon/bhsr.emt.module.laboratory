import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { TextFormat, translate } from 'react-jhipster';

import { APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, partialUpdateEntity } from './service-request.reducer';
import { Empty, Space, Table, Tag } from 'antd';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { ServiceRequestPriority } from 'app/shared/model/enumerations/service-request-priority.model';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import Swal from 'sweetalert2';
import { Link as MUILink } from '@mui/material';
import { FabButton } from 'app/entities/laboratory/shared/fab-button';
import { Add, Block, Start } from '@mui/icons-material';
import { IServiceRequestResponseLight } from 'app/shared/model/laboratory/service-request.model';
import { ColumnsType } from 'antd/es/table';

export const ServiceRequest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB_USER]));
  const isMedUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.MEDICAL_USER]));
  const serviceRequestList: [] = useAppSelector(state => state.laboratory.serviceRequest.entities);
  const loading = useAppSelector(state => state.laboratory.serviceRequest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const onCancel = serviceRequest => {
    const newServiceRequest = {
      id: serviceRequest.id,
      status: ServiceRequestStatus.REVOKED,
    };
    dispatch(partialUpdateEntity(newServiceRequest)).then(() => {
      dispatch(getEntities({}));
    });
  };

  const onStartRequest = serviceRequest => {
    const newServiceRequest = {
      id: serviceRequest.id,
      status: ServiceRequestStatus.ACTIVE,
    };
    dispatch(partialUpdateEntity(newServiceRequest)).then(() => {
      navigate(`${serviceRequest.id}`);
    });
  };

  const handleOpenCancel = serviceRequest => {
    Swal.fire({
      title: '¿Deseas continuar?',
      text: '¡No podrás deshacer esta acción!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: '¡Si, quiero eliminarlo!',
    }).then(result => {
      if (result.isConfirmed) {
        onCancel(serviceRequest);
      }
    });
  };

  const getTagColor = priority => {
    switch (ServiceRequestPriority[priority]) {
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

  const handleAdd = () => {
    navigate('new');
  };

  const columns: ColumnsType<IServiceRequestResponseLight> = [
    {
      title: '#',
      dataIndex: '',
      key: 'index',
      render: (text, record, index) => (
        <MUILink component={Link} to={`${record?.id}`}>
          {String(index + 1).padStart(6, '0')}
        </MUILink>
      ),
    },
    {
      title: 'Estado',
      dataIndex: 'status',
      key: 'status',
      render: (text, record) => translate(`laboratoryApp.ServiceRequestStatus.${record.status}`),
    },
    {
      title: 'Prioridad',
      dataIndex: 'priority',
      key: 'priority',
      render: (text, record) => (
        <Tag color={getTagColor(record.priority)}>
          {translate(`laboratoryApp.laboratoryServiceRequest.create.priority.${record.priority}`)}
        </Tag>
      ),
    },
    {
      title: 'Paciente',
      dataIndex: 'subject',
      key: 'subject',
      render: (text, record) => record?.subject?.name?.text,
    },
    {
      title: '# de reportes',
      dataIndex: 'diagnosticReportsFormats',
      key: 'diagnosticReportsFormats',
      render: (text, record) => record?.diagnosticReportsFormats?.length,
    },
    {
      title: 'Creado en',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (text, record) => <TextFormat value={record.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />,
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
      render: (text, record) => <TextFormat value={record.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />,
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
      render: (t, record) => (
        <>
          <Space size="middle">
            {isLabUser && record.status === ServiceRequestStatus.DRAFT && (
              <FabButton Icon={Start} onClick={() => onStartRequest(record)} color={'info'} />
            )}
            {isMedUser && record.status === ServiceRequestStatus.DRAFT && (
              <FabButton Icon={Block} onClick={() => handleOpenCancel(record?.id)} color={'error'} />
            )}
          </Space>
        </>
      ),
    },
  ];

  return (
    <>
      <PageHeader title={translate('laboratoryApp.laboratoryServiceRequest.home.title')} />
      {isMedUser && <FabButton Icon={Add} onClick={handleAdd} color={'secondary'} sx={{ color: 'white' }} />}
      {!serviceRequestList && loading && <p>Loading...</p>}
      <div className="table-responsive">
        {serviceRequestList && serviceRequestList.length > 0 ? (
          <>
            <Table
              onRow={(record, rowIndex) => {
                return {
                  onClick(event) {
                    navigate(`${record?.id}`);
                  },
                };
              }}
              dataSource={serviceRequestList}
              columns={columns}
              rowKey={'id'}
              sortDirections={['descend', 'ascend']}
            />
          </>
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={'No hay solicitudes de servicios de diagnóstico'} />
        )}
      </div>
    </>
  );
};

export default ServiceRequest;
