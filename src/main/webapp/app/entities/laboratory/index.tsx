import React from 'react';
import { Breadcrumb, Layout } from 'antd';
import Sider from 'antd/es/layout/Sider';
import { Content } from 'antd/es/layout/layout';
import Menu, { MenuProps } from 'antd/es/menu';
import UserOutlined from '@ant-design/icons/UserOutlined';
import PlusOutlined from '@ant-design/icons/PlusOutlined';
import LaptopOutlined from '@ant-design/icons/LaptopOutlined';
import NotificationOutlined from '@ant-design/icons/NotificationOutlined';
import { Navigate, Outlet, useNavigate } from 'react-router-dom';

export const LaboratoryHome = () => {
  const navigate = useNavigate();
  const items2: MenuProps['items'] = [
    {
      label: 'Formats Management',
      subMenus: [
        { key: 'getAll-diagnosticReport', label: 'Home', href: '/laboratory/diagnostic-report-format', icon: UserOutlined },
        { key: 'create-diagnosticReport', label: 'Create', href: '/laboratory/diagnostic-report-format/new', icon: PlusOutlined },
      ],
    },
    {
      label: 'Constants Management',
      subMenus: [
        { key: 'getAll-constants', label: 'Home', href: '/laboratory/value-set', icon: UserOutlined },
        { key: 'create-constant', label: 'Create', href: '/laboratory/value-set/new', icon: PlusOutlined },
      ],
    },
    {
      label: 'General settings',
      subMenus: [
        { key: 'getAll-settings', label: 'Settings', href: '/laboratory/identifier-type', icon: null },
        { key: 'create-settings', label: 'Create', href: '/laboratory/identifier-type/new', icon: null },
        { key: 'getAll-roles', label: 'Roles', href: '/laboratory/roles-management', icon: null },
      ],
    },
  ].map((topMenu, index) => {
    const key = String(index + 1);

    return {
      key: `${key}`,
      label: `${topMenu.label}`,

      children: topMenu.subMenus.map((subMenu, j) => {
        return {
          key: subMenu.key,
          label: subMenu.label,
          icon: subMenu.icon && React.createElement(subMenu.icon),
          onClick() {
            navigate(subMenu.href);
          },
        };
      }),
    };
  });
  return (
    <Layout>
      <Sider width={200}>
        <Menu
          mode="inline"
          defaultSelectedKeys={['1']}
          defaultOpenKeys={['sub1']}
          style={{ height: '100%', borderRight: 0 }}
          items={items2}
        />
      </Sider>
      <Layout style={{ padding: '0 24px 24px' }}>
        <Breadcrumb style={{ margin: '16px 0' }}>
          <Breadcrumb.Item>Home</Breadcrumb.Item>
          <Breadcrumb.Item>Laboratory</Breadcrumb.Item>
          <Breadcrumb.Item>Diagnostic Report Format</Breadcrumb.Item>
        </Breadcrumb>
        <Content
          style={{
            padding: 24,
            margin: 0,
            minHeight: 280,
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};
