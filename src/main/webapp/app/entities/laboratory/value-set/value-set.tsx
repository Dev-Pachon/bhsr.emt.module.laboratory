import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Link as MUILink } from '@mui/material';
import { translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IValueSet } from 'app/shared/model/laboratory/value-set.model';
import { deleteEntity, getEntities } from './value-set.reducer';
import { Empty, Space, Table } from 'antd';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import Swal from 'sweetalert2';
import { FabButton } from 'app/entities/laboratory/shared/fab-button';
import { Add, Delete, Edit } from '@mui/icons-material';
import { ColumnsType } from 'antd/es/table';

export const ValueSet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const valueSetList = useAppSelector(state => state.laboratory.valueSet.entities);
  const loading = useAppSelector(state => state.laboratory.valueSet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

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

  const columns: ColumnsType<IValueSet> = [
    {
      title: 'Nombre del formato',
      dataIndex: 'name',
      key: 'format',
      render: (text, record) => (
        <MUILink component={Link} to={`${record?.id}`} color={'#00f'}>
          {record?.name}
        </MUILink>
      ),
    },
    {
      title: 'Número de constantes',
      dataIndex: 'constants',
      key: 'constants',
      render: (text, record) => record?.constants?.length,
    },
    {
      title: 'Tipo de dato',
      dataIndex: 'dataType',
      key: 'dataType',
      render: (text, record) => translate(`laboratoryApp.fieldFormatType.${record.dataType}`),
    },
    {
      title: 'Descripción',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Acción',
      dataIndex: '',
      key: 'x',
      render: (text, record) => (
        <Space size="middle">
          <FabButton Icon={Edit} onClick={() => handleEdit(record?.id)} color={'info'} />

          <FabButton Icon={Delete} onClick={() => handleOpenDelete(record?.id)} color={'error'} />
        </Space>
      ),
    },
  ];

  const handleEdit = (id: string) => {
    navigate(id + '/edit');
  };

  const handleAdd = () => {
    navigate('new');
  };

  return (
    <div>
      <PageHeader title={translate('laboratoryApp.laboratoryValueSet.home.title')} />
      <FabButton Icon={Add} onClick={handleAdd} color={'secondary'} sx={{ color: 'white' }} />
      <div className="table-responsive">
        {valueSetList && valueSetList.length > 0 ? (
          <>
            <Table columns={columns} dataSource={valueSetList} rowKey={r => r.id} />
          </>
        ) : (
          !loading && <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={'Sin conjuntos de constantes'} />
        )}
      </div>
    </div>
  );
};

export default ValueSet;
