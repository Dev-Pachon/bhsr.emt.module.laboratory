import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { getEntities } from './patient.reducer';

export const Patient = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const patientList = useAppSelector(state => state.laboratory.patient.entities);
  const loading = useAppSelector(state => state.laboratory.patient.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="patient-heading" data-cy="PatientHeading">
        <Translate contentKey="laboratoryApp.laboratoryPatient.home.title">Patients</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="laboratoryApp.laboratoryPatient.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/laboratory/patient/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="laboratoryApp.laboratoryPatient.home.createLabel">Create new Patient</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {patientList && patientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryPatient.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryPatient.active">Active</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryPatient.gender">Gender</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryPatient.birthDate">Birth Date</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {patientList.map((patient, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/laboratory/patient/${patient.id}`} color="link" size="sm">
                      {patient.id}
                    </Button>
                  </td>
                  <td>{patient.active ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`laboratoryApp.AdministrativeGender.${patient.gender}`} />
                  </td>
                  <td>{patient.birthDate ? <TextFormat type="date" value={patient.birthDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/laboratory/patient/${patient.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/laboratory/patient/${patient.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/laboratory/patient/${patient.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="laboratoryApp.laboratoryPatient.home.notFound">No Patients found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Patient;
