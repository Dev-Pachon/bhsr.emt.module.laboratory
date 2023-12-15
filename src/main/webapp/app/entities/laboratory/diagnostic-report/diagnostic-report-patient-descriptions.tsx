import { Descriptions } from 'antd';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import React from 'react';
import { translate } from 'react-jhipster';

export const DiagnosticReportPatientDescriptions = ({ subject }) => {
  const getPatientItems = (subjectEl: IPatient) => {
    return [
      {
        key: 'name',
        label: 'Name',
        children: subjectEl?.name?.text,
      },
      {
        key: 'identifierType',
        label: 'Id Type',
        children: subjectEl?.identifier?.type?.name,
      },
      {
        key: 'identifier',
        label: 'Id',
        children: subjectEl?.identifier?.value,
      },
      {
        key: 'gender',
        label: 'Gender',
        children: translate(`laboratoryApp.AdministrativeGender.${subjectEl?.gender}`),
      },
      {
        key: 'birthDate',
        label: 'Birth Date',
        children: subjectEl?.birthDate,
      },
      {
        key: 'address',
        label: 'Address',
        children: subjectEl?.address?.text,
      },
    ];
  };

  return <Descriptions title={'Patient'} bordered size={'small'} items={getPatientItems(subject)} />;
};

export default DiagnosticReportPatientDescriptions;
