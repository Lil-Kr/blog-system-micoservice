import React from 'react'
import { Outlet } from 'oh-router-react'
import { Layout } from 'antd/lib'

const { Content } = Layout

const ContentLayout = () => {
  return (
    <>
      <Content
        className='site-layout-background content-layout'
        style={{
          padding: 0,
          margin: 0,
          height: '100%',
          overflow: 'hidden',
          display: 'flex',
          flexDirection: 'column'
        }}
      >
        <div style={{ flex: 1, overflow: 'hidden' }}>
          <Outlet />
        </div>
      </Content>
    </>
  )
}

export default ContentLayout
