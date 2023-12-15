import { IPatient } from 'app/shared/model/laboratory/patient.model';
import { AdministrativeGender } from 'app/shared/model/enumerations/administrative-gender.model';
import React, { useState } from 'react';
import { Empty, Input, Space, Table } from 'antd';
import ServiceRequestModal from 'app/entities/laboratory/service-request/service-request-update';
import { translate } from 'react-jhipster';
import PageHeader from 'app/entities/laboratory/shared/page-header';
import { Link } from 'react-router-dom';
import { LeftOutlined } from '@ant-design/icons';
import { SearchProps } from 'antd/es/input';
import axios from 'axios';

const columns = [
  {
    title: 'Nombre',
    dataIndex: 'name',
    key: 'name',
    render: (name: any) => name?.text,
  },
  {
    title: 'Tipo de Identificación',
    dataIndex: 'identifier',
    key: 'identifierType',
    render: (identifier: { type: { name: any } }) => identifier?.type?.name,
  },
  {
    title: 'Id',
    dataIndex: 'identifier',
    key: 'identifier',
    render: (identifier: any) => identifier?.value,
  },
  {
    title: 'Fecha de Nacimiento',
    dataIndex: 'birthDate',
    key: 'birthDate',
  },
  {
    title: 'Género',
    dataIndex: 'gender',
    key: 'gender',
    render: (gender: AdministrativeGender) => translate(`laboratoryApp.AdministrativeGender.${AdministrativeGender[gender]}`),
  },
  {
    title: 'Acción',
    dataIndex: '',
    key: 'x',
    render: (t, record) => <ServiceRequestModal patient={record} />,
  },
];

const PatientListToRequestService = () => {
  const [patientList, setPatientList] = useState<IPatient[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const onSearch: SearchProps['onSearch'] = (value, _e, info) => {
    setLoading(true);
    const requestUrl = `services/laboratory/api/patients?name=${'idNumber'}&value=${value}`;
    axios
      .get<IPatient[]>(requestUrl)
      .then(
        response => {
          setPatientList(response.data);
        },
        error => {
          console.log(error);
        }
      )
      .finally(() => {
        setLoading(false);
      });
  };

  return (
    <>
      <PageHeader
        title={'Lista de Pacientes'}
        leftAction={
          <Link to={`/laboratory/service-request`}>
            <LeftOutlined style={{ fontSize: '24px', color: 'white' }} rev={undefined} />
          </Link>
        }
      />
      <Space size="middle" direction={'vertical'} style={{ width: '100%' }}>
        <Input.Search
          placeholder="Ingrese el número de documento del paciente..."
          allowClear
          onSearch={onSearch}
          size="large"
          loading={loading}
        />
        {patientList && patientList.length > 0 ? (
          <Table dataSource={patientList} columns={columns} rowKey={'id'} />
        ) : (
          <Empty description={'Oops, no hay pacientes con dicha identificación...'} />
        )}
      </Space>
    </>
  );
};

export default PatientListToRequestService;
