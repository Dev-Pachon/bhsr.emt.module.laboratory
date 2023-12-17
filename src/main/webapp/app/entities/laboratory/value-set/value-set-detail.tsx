import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './value-set.reducer';
import { Space, Table, Typography } from 'antd';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { translate } from 'react-jhipster';
import { LeftOutlined } from '@ant-design/icons';

const { Title } = Typography;

const columns = [
  {
    title: 'Nombre',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Descripción',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: 'Valor',
    dataIndex: 'value',
    key: 'value',
  },
];

export const ValueSetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const valueSetEntity = useAppSelector(state => state.laboratory.valueSet.entity);
  return (
    <>
      <PageHeader
        title={translate('laboratoryApp.laboratoryValueSet.detail.title')}
        leftAction={
          <Link to={`/laboratory/value-set`} style={{ placeSelf: 'end' }}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Space direction="vertical" size="middle" style={{ display: 'flex' }}>
        <Title level={3}>{valueSetEntity.name}</Title>
        <Typography>{valueSetEntity.description}</Typography>
        <Table columns={columns} dataSource={valueSetEntity.constants}></Table>
      </Space>
    </>
  );
};

export default ValueSetDetail;
