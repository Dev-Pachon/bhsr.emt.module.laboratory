import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { getEntities } from './diagnostic-report-format.reducer';

export const DiagnosticReportFormat = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const diagnosticReportFormatList = useAppSelector(state => state.laboratory.diagnosticReportFormat.entities);
  // const diagnosticReportFormatList = [
  //   {
  //     id: 1,
  //     name: 'name 1',
  //     createdAt: '2021-08-01',
  //     createdBy: 'createdBy 1',
  //     updatedAt: '2021-08-01',
  //     updatedBy: 'updatedBy 1',
  //   },
  //   {
  //     id: 2,
  //     name: 'name 2',
  //     createdAt: '2021-08-02',
  //     createdBy: 'createdBy 2',
  //     updatedAt: '2021-08-02',
  //     updatedBy: 'updatedBy 2',
  //   },
  // ];
  const loading = useAppSelector(state => state.laboratory.diagnosticReportFormat.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="diagnostic-report-format-heading" data-cy="DiagnosticReportFormatHeading">
        <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.title">Diagnostic Report Formats</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/laboratory/diagnostic-report-format/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.createLabel">
              Create new Diagnostic Report Format
            </Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {diagnosticReportFormatList && diagnosticReportFormatList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.updatedBy">Updated By</Translate>
                </th>

                <th />
              </tr>
            </thead>
            <tbody>
              {diagnosticReportFormatList.map((diagnosticReportFormat, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/laboratory/diagnostic-report-format/${diagnosticReportFormat.id}`} color="link" size="sm">
                      {diagnosticReportFormat.id}
                    </Button>
                  </td>
                  <td>{diagnosticReportFormat.name}</td>
                  <td>
                    {diagnosticReportFormat.createdAt ? (
                      <TextFormat type="date" value={diagnosticReportFormat.createdAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{diagnosticReportFormat.createdBy}</td>
                  <td>
                    {diagnosticReportFormat.updatedAt ? (
                      <TextFormat type="date" value={diagnosticReportFormat.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{diagnosticReportFormat.updatedBy}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/laboratory/diagnostic-report-format/${diagnosticReportFormat.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/laboratory/diagnostic-report-format/${diagnosticReportFormat.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/laboratory/diagnostic-report-format/${diagnosticReportFormat.id}/delete`}
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
              <Translate contentKey="laboratoryApp.laboratoryDiagnosticReportFormat.home.notFound">
                No Diagnostic Report Formats found
              </Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DiagnosticReportFormat;
