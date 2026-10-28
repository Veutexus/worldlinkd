import { vitePreprocess } from '@sveltejs/vite-plugin-svelte'

// The indented-syntax sass compiler rejects leading indentation on top-level
// statements (error: "Indenting at the beginning of the document is illegal").
// Older tooling silently stripped it; the current svelte plugin passes the raw
// <style lang="sass"> content through. Dedent uniformly-indented style blocks
// before handing them to sass. Whitespace-only, no functional change.
function dedentSass() {
  return {
    name: 'dedent-sass',
    style({ content, attributes }) {
      if (attributes.lang !== 'sass') return
      const lines = content.split('\n')
      const minIndent = Math.min(
        ...lines
          .filter((l) => l.trim().length > 0)
          .map((l) => l.match(/^\s*/)[0].length),
      )
      if (minIndent === 0) return
      return { code: lines.map((l) => l.slice(minIndent)).join('\n') }
    },
  }
}

export default {
  // Consult https://svelte.dev/docs#compile-time-svelte-preprocess
  // for more information about preprocessors
  preprocess: [dedentSass(), vitePreprocess()],
}
