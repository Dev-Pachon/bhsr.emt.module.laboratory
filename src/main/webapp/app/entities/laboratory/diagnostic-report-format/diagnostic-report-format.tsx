import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Table } from 'reactstrap';
import { TextFormat, translate } from 'react-jhipster';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntities } from './diagnostic-report-format.reducer';
import { Empty, Typography } from 'antd';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { Link as MUILink } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import Swal from 'sweetalert2';
import { Add, Delete, Edit } from '@mui/icons-material';
import { FabButton } from 'app/entities/laboratory/shared/fab-button';

const { Title } = Typography;

export const DiagnosticReportFormat = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const diagnosticReportFormatList = useAppSelector(state => state.laboratory.diagnosticReportFormat.entities);
  const loading = useAppSelector(state => state.laboratory.diagnosticReportFormat.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleOpenDelete = (id: string) => {
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
        dispatch(deleteEntity(id));
      }
    });
  };

  const handleAdd = () => {
    navigate('new');
  };
  const handleEdit = (id: string) => {
    navigate(id + '/edit');
  };

  return (
    <>
      <CssBaseline />
      <PageHeader
        title={translate('laboratoryApp.laboratoryDiagnosticReportFormat.home.title')}
        // rightAction={
        //   <Link to={`new`}>
        //     <PlusOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
        //   </Link>
        // }
      />
      <FabButton Icon={Add} onClick={handleAdd} color={'secondary'} sx={{ color: 'white' }} />
      <div className="table-responsive">
        {diagnosticReportFormatList && diagnosticReportFormatList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Nombre del formato</th>
                <th>Creado en</th>
                <th>Creado por</th>
                <th>Última actualización en</th>
                <th>Última actualización por</th>

                <th />
              </tr>
            </thead>
            <tbody>
              {diagnosticReportFormatList.map((diagnosticReportFormat, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <MUILink component={Link} to={`${diagnosticReportFormat?.id}`} color={'#00f'}>
                      {diagnosticReportFormat?.name}
                    </MUILink>
                  </td>
                  <td>
                    {diagnosticReportFormat?.createdAt ? (
                      <TextFormat type="date" value={diagnosticReportFormat?.createdAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{`${diagnosticReportFormat?.createdBy?.firstName} ${diagnosticReportFormat?.createdBy?.lastName}`}</td>
                  <td>
                    {diagnosticReportFormat?.updatedAt ? (
                      <TextFormat type="date" value={diagnosticReportFormat?.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{`${diagnosticReportFormat?.updatedBy?.firstName} ${diagnosticReportFormat?.updatedBy?.lastName}`}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <FabButton Icon={Edit} onClick={() => handleEdit(diagnosticReportFormat?.id)} color={'info'} />

                      <FabButton Icon={Delete} onClick={() => handleOpenDelete(diagnosticReportFormat?.id)} color={'error'} />
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={'No hay formatos de reporte de diagnóstico...'} />
        )}
      </div>
    </>
  );
};

export default DiagnosticReportFormat;
