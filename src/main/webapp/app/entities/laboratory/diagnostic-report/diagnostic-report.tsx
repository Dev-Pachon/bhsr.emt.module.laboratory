import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDiagnosticReport } from 'app/shared/model/laboratory/diagnostic-report.model';
import { getEntities } from './diagnostic-report.reducer';
import { Empty } from 'antd';

export const DiagnosticReport = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const diagnosticReportList = useAppSelector(state => state.laboratory.diagnosticReport.entities);
  const loading = useAppSelector(state => state.laboratory.diagnosticReport.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="diagnostic-report-heading" data-cy="DiagnosticReportHeading">
        <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.home.title">Diagnostic Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/laboratory/diagnostic-report/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.home.createLabel">Create new Diagnostic Report</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {diagnosticReportList && diagnosticReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.deletedAt">Deleted At</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.subject">Subject</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.format">Format</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryDiagnosticReport.basedOn">Based On</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {diagnosticReportList.map((diagnosticReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/laboratory/diagnostic-report/${diagnosticReport.id}`} color="link" size="sm">
                      {diagnosticReport.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`laboratoryApp.DiagnosticReportStatus.${diagnosticReport.status}`} />
                  </td>
                  <td>
                    {diagnosticReport.createdAt ? (
                      <TextFormat type="date" value={diagnosticReport.createdAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{diagnosticReport.createdBy}</td>
                  <td>
                    {diagnosticReport.updatedAt ? (
                      <TextFormat type="date" value={diagnosticReport.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{diagnosticReport.updatedBy}</td>
                  <td>
                    {diagnosticReport.deletedAt ? (
                      <TextFormat type="date" value={diagnosticReport.deletedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {diagnosticReport.subject ? (
                      <Link to={`/laboratory/patient/${diagnosticReport.subject.id}`}>{diagnosticReport.subject.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {diagnosticReport.format ? (
                      <Link to={`/laboratory/diagnostic-report-format/${diagnosticReport.format.id}`}>{diagnosticReport.format.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {diagnosticReport.basedOn ? (
                      <Link to={`/laboratory/service-request/${diagnosticReport.basedOn.id}`}>{diagnosticReport.basedOn.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/laboratory/diagnostic-report/${diagnosticReport.id}`}
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
                        to={`/laboratory/diagnostic-report/${diagnosticReport.id}/edit`}
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
                        to={`/laboratory/diagnostic-report/${diagnosticReport.id}/delete`}
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
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
        )}
      </div>
    </div>
  );
};

export default DiagnosticReport;
