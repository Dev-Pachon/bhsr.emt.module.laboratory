import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Table } from 'reactstrap';
import { TextFormat, Translate, translate } from 'react-jhipster';

import { APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, partialUpdateEntity } from './service-request.reducer';
import { Empty, Tag } from 'antd';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';
import { ServiceRequestPriority } from 'app/shared/model/enumerations/service-request-priority.model';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import Swal from 'sweetalert2';
import { Button } from '@mui/material';
import { FabButton } from 'app/entities/laboratory/shared/fab-button';
import { Add } from '@mui/icons-material';

export const ServiceRequest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB]));
  const isMedUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.MED]));
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

  const handleAdd = () => {
    navigate('new');
  };

  return (
    <>
      <PageHeader title={translate('laboratoryApp.laboratoryServiceRequest.home.title')} />
      {isMedUser && <FabButton Icon={Add} onClick={handleAdd} color={'secondary'} sx={{ color: 'white' }} />}
      {!serviceRequestList && loading && <p>Loading...</p>}
      <div className="table-responsive">
        {serviceRequestList && serviceRequestList.length > 0 ? (
          <>
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.status">Status</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.priority">Priority</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryPatient.detail.title">Subject</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.diagnosticReportsIds"># of diagnostic reports</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.createdAt">Created At</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.createdBy">Created By</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.updatedAt">Updated At</Translate>
                  </th>
                  <th>
                    <Translate contentKey="laboratoryApp.laboratoryServiceRequest.updatedBy">Updated By</Translate>
                  </th>
                  <th>Acción</th>
                </tr>
              </thead>
              <tbody>
                {[...serviceRequestList]
                  .sort((actual: any, previous: any) => previous?.id?.localeCompare(actual?.id))
                  .map((serviceRequest: any, i: number) => (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>
                        <Translate contentKey={`laboratoryApp.ServiceRequestStatus.${serviceRequest.status}`} />
                      </td>
                      <td>
                        <Tag color={getTagColor(serviceRequest)}>
                          {translate(`laboratoryApp.laboratoryServiceRequest.create.priority.${serviceRequest.priority}`)}
                        </Tag>
                      </td>
                      <td>{serviceRequest.subject?.name?.text}</td>
                      <td>{serviceRequest.diagnosticReportsFormats?.length}</td>
                      <td>
                        {serviceRequest.createdAt ? (
                          <TextFormat type="date" value={serviceRequest.createdAt} format={APP_LOCAL_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td>{`${serviceRequest.createdBy.firstName} ${serviceRequest.createdBy.lastName}`}</td>
                      <td>
                        {serviceRequest.updatedAt ? (
                          <TextFormat type="date" value={serviceRequest.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                        ) : null}
                      </td>
                      <td>{`${serviceRequest.updatedBy.firstName} ${serviceRequest.updatedBy.lastName}`}</td>
                      <td className="text-end">
                        <div className="btn-group flex-btn-group-container">
                          <Button
                            component={Link}
                            to={`/laboratory/service-request/${serviceRequest.id}`}
                            variant="contained"
                            color={'info'}
                          >
                            Ver
                          </Button>
                          {isLabUser && serviceRequest.status === ServiceRequestStatus.DRAFT && (
                            <Button onClick={() => onStartRequest(serviceRequest)} variant="contained" color={'primary'}>
                              Iniciar solicitud
                            </Button>
                          )}
                          {isMedUser && serviceRequest.status === ServiceRequestStatus.DRAFT && (
                            <Button onClick={() => handleOpenCancel(serviceRequest)} variant="contained" color={'error'}>
                              Cancelar
                            </Button>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </Table>
          </>
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={'No hay solicitudes de servicios de diagnóstico'} />
        )}
      </div>
    </>
  );
};

export default ServiceRequest;
