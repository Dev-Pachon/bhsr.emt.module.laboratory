import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IServiceRequest } from 'app/shared/model/laboratory/service-request.model';
import { getEntities, partialUpdateEntity } from './service-request.reducer';
import { Empty } from 'antd';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ServiceRequestModal from 'app/entities/laboratory/service-request/service-request-update';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import { ServiceRequestStatus } from 'app/shared/model/enumerations/service-request-status.model';

const samplePatient: IPatient = {
  id: '1',
  name: {
    text: 'John Doe',
    given: 'John',
    family: 'Doe',
  },
  active: true,
  gender: AdministrativeGender.MALE,
  birthDate: '1990-01-01',
  address: {
    line: '123 Main St',
    city: 'Anytown',
    state: 'NY',
    country: 'US',
    text: '123 Main St, Anytown, NY, US',
    district: 'Anydistrict',
  },
  contact: {
    address: {
      line: '465 Main St',
      city: 'Anytown',
      state: 'SF',
      country: 'US',
      text: '465 Main St, Anytown, SF, US',
      district: 'Anydistrict',
    },
    gender: AdministrativeGender.FEMALE,
    name: {
      text: 'Jane Doe',
      given: 'Jane',
      family: 'Doe',
    },
    id: '2',
  },
};

export const ServiceRequest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const isLabUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.LAB]));
  const isMedUser = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.MED]));
  const serviceRequestList = useAppSelector(state => state.laboratory.serviceRequest.entities);
  const loading = useAppSelector(state => state.laboratory.serviceRequest.loading);

  useEffect(() => {
    dispatch(getEntities({}));
    console.log(`isLabUser: ${isLabUser}`);
  }, []);

  const onCancel = (serviceRequest: IServiceRequest) => {
    const newServiceRequest = {
      ...serviceRequest,
      status: ServiceRequestStatus.REVOKED,
    };

    dispatch(partialUpdateEntity(newServiceRequest));
    dispatch(getEntities({}));
  };

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="service-request-heading" data-cy="ServiceRequestHeading">
        <Translate contentKey="laboratoryApp.laboratoryServiceRequest.home.title">Service Requests</Translate>
        <div className="d-flex justify-content-end">
          <ServiceRequestModal patient={samplePatient} />
        </div>
      </h2>
      <div className="table-responsive">
        {serviceRequestList && serviceRequestList.length > 0 ? (
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
                <th />
              </tr>
            </thead>
            <tbody>
              {serviceRequestList.map((serviceRequest: IServiceRequest, i: number) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Translate contentKey={`laboratoryApp.ServiceRequestStatus.${serviceRequest.status}`} />
                  </td>
                  <td>{translate(`laboratoryApp.laboratoryServiceRequest.create.priority.${serviceRequest.priority}`)}</td>
                  <td>{serviceRequest.diagnosticReportsIds.length}</td>
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
                        tag={Link}
                        to={`/laboratory/service-request/${serviceRequest.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      {isLabUser && (
                        <Button
                          tag={Link}
                          to={`/laboratory/service-request/${serviceRequest.id}`}
                          color="primary"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.start" interpolate={{ entity: 'Request' }}>
                              Start Request
                            </Translate>
                          </span>
                        </Button>
                      )}
                      {isMedUser && (
                        <Button color="danger" size="sm" onClick={() => onCancel(serviceRequest)}>
                          <FontAwesomeIcon icon="cancel" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.cancel" interpolate={{ entity: 'Request' }}>
                              Cancel Request
                            </Translate>
                          </span>
                        </Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        )}
      </div>
    </div>
  );
};

export default ServiceRequest;
