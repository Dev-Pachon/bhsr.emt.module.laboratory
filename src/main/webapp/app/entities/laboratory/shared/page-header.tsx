import { AppBar, Box, Typography } from '@mui/material';
import React from 'react';
import { capitalizeFirstLetter } from 'app/shared/util/string-utils';

const PageHeader = ({ title, rightAction = null, leftAction = null }) => (
  <Box sx={{ flexGrow: 1 }}>
    <AppBar position="static" sx={{ mb: 2, p: 1, flexDirection: 'row' }}>
      {leftAction && <Box sx={{ flexGrow: 1, placeSelf: 'end', position: 'absolute' }}>{leftAction}</Box>}
      <Typography variant="h5" component="div" align="center" sx={{ flexGrow: 20 }}>
        {capitalizeFirstLetter(title)}
      </Typography>
      {rightAction && <Box sx={{ flexGrow: 1 }}>{rightAction}</Box>}
    </AppBar>
  </Box>
);

export default PageHeader;
