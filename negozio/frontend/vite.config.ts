import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: '../src/main/resources/static/js/react',
    emptyOutDir: false,
    rollupOptions: {
      output: {
        entryFileNames: `react-reviews.js`,
        chunkFileNames: `react-reviews.js`,
        assetFileNames: `[name].[ext]`
      }
    }
  }
})
