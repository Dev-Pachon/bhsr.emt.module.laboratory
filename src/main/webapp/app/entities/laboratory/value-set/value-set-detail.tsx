import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './value-set.reducer';
import { Space, Table, Typography } from 'antd';
const { Title } = Typography;

const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Description',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: 'Value',
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
    <Space direction="vertical" size="middle" style={{ display: 'flex' }}>
      <Title level={2}>{valueSetEntity.name}</Title>
      <Typography>{valueSetEntity.description}</Typography>
      <Table columns={columns} dataSource={valueSetEntity.constants}></Table>
    </Space>
  );
};

export default ValueSetDetail;
