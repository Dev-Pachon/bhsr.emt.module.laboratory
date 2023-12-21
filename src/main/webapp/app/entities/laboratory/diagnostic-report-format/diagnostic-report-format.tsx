import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { TextFormat, translate } from 'react-jhipster';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteEntity, getEntities } from './diagnostic-report-format.reducer';
import { Empty, Space, Table, Typography } from 'antd';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { Link as MUILink } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import Swal from 'sweetalert2';
import { Add, Delete, Edit } from '@mui/icons-material';
import { FabButton } from 'app/entities/laboratory/shared/fab-button';
import { IDiagnosticReportFormat } from 'app/shared/model/laboratory/diagnostic-report-format.model';
import { ColumnsType } from 'antd/es/table';

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
      cancelButtonText: 'Cancelar',
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

  const columns: ColumnsType<IDiagnosticReportFormat> = [
    {
      title: 'Nombre del formato',
      dataIndex: 'name',
      key: 'format',
      render: (text, record) => (
        <MUILink component={Link} to={`${record?.id}`}>
          {record?.name}
        </MUILink>
      ),
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
          <FabButton Icon={Edit} onClick={() => handleEdit(record?.id)} color={'info'} tooltip={'Editar formato'} />

          <FabButton Icon={Delete} onClick={() => handleOpenDelete(record?.id)} color={'error'} tooltip={'Eliminar formato'} />
        </Space>
      ),
    },
  ];

  return (
    <>
      <CssBaseline />
      <PageHeader title={translate('laboratoryApp.laboratoryDiagnosticReportFormat.home.title')} />
      <FabButton Icon={Add} onClick={handleAdd} color={'secondary'} sx={{ color: 'white' }} tooltip={'Crear formato'} />
      <div className="table-responsive">
        {diagnosticReportFormatList && diagnosticReportFormatList.length > 0 ? (
          <Table
            columns={columns}
            dataSource={[...diagnosticReportFormatList].sort((a, b) => a?.id?.localeCompare(b?.id))}
            rowKey={r => r.id}
          />
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={'No hay formatos de reporte de diagnóstico...'} />
        )}
      </div>
    </>
  );
};

export default DiagnosticReportFormat;
