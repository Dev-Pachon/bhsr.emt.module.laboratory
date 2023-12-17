import React, { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Table } from 'reactstrap';
import { Button } from '@mui/material';
import { translate, Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IValueSet } from 'app/shared/model/laboratory/value-set.model';
import { deleteEntity, getEntities } from './value-set.reducer';
import { Empty } from 'antd';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { PlusOutlined } from '@ant-design/icons';
import Swal from 'sweetalert2';

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

  return (
    <div>
      <PageHeader
        title={translate('laboratoryApp.laboratoryValueSet.home.title')}
        rightAction={
          <Link to={`new`}>
            <PlusOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <div className="table-responsive">
        {valueSetList && valueSetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryValueSet.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryValueSet.numConstants">Number of Constants</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryValueSet.dataType">Datatype</Translate>
                </th>
                <th>
                  <Translate contentKey="laboratoryApp.laboratoryValueSet.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {valueSetList.map((valueSet: IValueSet, i: number) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{valueSet.name}</td>
                  <td>{valueSet.constants.length}</td>
                  <td>{translate(`laboratoryApp.fieldFormatType.${valueSet.dataType}`)}</td>
                  <td>{valueSet.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button component={Link} to={`/laboratory/value-set/${valueSet.id}`} variant="contained" color={'info'}>
                        Ver
                      </Button>
                      <Button component={Link} to={`/laboratory/value-set/${valueSet.id}/edit`} variant="contained" color={'info'}>
                        Editar
                      </Button>

                      <Button onClick={() => handleOpenDelete(valueSet?.id)} variant="contained" color={'error'}>
                        Eliminar
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

export default ValueSet;
