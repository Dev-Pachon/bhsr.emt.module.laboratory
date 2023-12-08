import { Descriptions } from 'antd';
import { IPatient } from 'app/shared/model/laboratory/patient.model';
import React from 'react';

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
        children: subjectEl?.identifierType?.name,
      },
      {
        key: 'identifier',
        label: 'Id',
        children: subjectEl?.identifier,
      },
      {
        key: 'gender',
        label: 'Gender',
        children: subjectEl?.gender,
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
